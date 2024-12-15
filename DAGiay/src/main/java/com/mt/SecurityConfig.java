package com.mt;

import java.util.NoSuchElementException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mt.entity.Account;

import com.mt.service.AccountService;
import com.mt.util.PasswordUtil;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService accountService;



    
//    @Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(username -> {
//			try {
//				Account user = accountService.findByUsername(username);
//				String password = passwordUtil.getBcryBCryptPasswordEncoder().encode(user.getPassword());
//				
//				String role = user.isRole() ? "ADMIN" : "USER";
//				return User.withUsername(username)
//                        .password(password)
//                        .roles(role)
//                        .build();
//			} catch (NoSuchElementException e) {
//				throw new UsernameNotFoundException(username + "not found");
//			}
//		}); // Thêm password encoder ở đây
//	}
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            try {
                Account user = accountService.findByUsername(username);
                String password = user.getPassword(); // Lấy mật khẩu từ cơ sở dữ liệu
                
                String role = user.isRole() ? "ADMIN" : "USER"; // Xác định vai trò của người dùng
                return User.withUsername(username)
                        .password(password) // Thiết lập tên đăng nhập và mật khẩu
                        .roles(role) // Thiết lập vai trò
                        .build();
            } catch (NoSuchElementException e) {
                throw new UsernameNotFoundException(username + " không tồn tại"); // Nếu không tìm thấy người dùng
            }
        }).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString(); // Trả về mật khẩu gốc để so sánh
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                // Kiểm tra xem mật khẩu được lưu trữ có phải là mật khẩu đã mã hóa không
                if (encodedPassword.length() == 60) { // Giả sử mật khẩu mã hóa bcrypt có độ dài 60 ký tự
                    return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword); // So sánh với mật khẩu mã hóa
                }
                // Nếu là mật khẩu chưa mã hóa, so sánh trực tiếp
                return rawPassword.toString().equals(encodedPassword);
            }
        });
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // Cấu hình các yêu cầu URL với phân quyền phù hợp
        http.authorizeRequests()
        .antMatchers("/change-password").permitAll()
        .antMatchers("/", "/register").permitAll()
            .antMatchers("/order/**").hasAnyRole("USER", "ADMIN")  // Chỉ người dùng có vai trò USER hoặc ADMIN mới truy cập được /order/**
            .antMatchers("/admin/**").hasRole("ADMIN")  // Chỉ người dùng có vai trò ADMIN mới truy cập được /admin/**
            .antMatchers("/rest/authorities").hasRole("ADMIN")  // Chỉ người dùng có vai trò ADMIN mới truy cập được /rest/authorities
            
            .anyRequest().permitAll();  // Tất cả các yêu cầu khác được phép mà không cần xác thực

        // Cấu hình form đăng nhập
        http.formLogin()
            .loginPage("/security/login/form")
            .loginProcessingUrl("/security/login")
            .defaultSuccessUrl("/security/login/success", false)
            .failureUrl("/security/login/error");


        // Cấu hình remember-me
        http.rememberMe()
            .tokenValiditySeconds(86400);  // Token có hiệu lực trong 1 ngày

        // Cấu hình xử lý lỗi truy cập bị từ chối
        http.exceptionHandling()
            .accessDeniedPage("/security/unauthoried");
        // Cấu hình logout
        http.logout()
            .logoutUrl("/security/logoff")
            .logoutSuccessUrl("/security/logoff/success");
        //login facebook,google
        http.oauth2Login()
        .loginPage("/auth/login/form")
        .defaultSuccessUrl("/oauth2/login/success", true)
        .failureUrl("/auth/login/error")
        .authorizationEndpoint()
        .baseUri("/oauth2/authorization");
    }

    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
    
    
}
