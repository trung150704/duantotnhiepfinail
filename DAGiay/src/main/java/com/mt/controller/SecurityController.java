package com.mt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import com.mt.entity.Account;
import com.mt.service.AccountService;


import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController {
	
	@Autowired
	AccountService accountService;
	@RequestMapping("/security/login/form")
	public String loginForm(Model model) {
		model.addAttribute("message","Vui lòng đăng nhập");
		model.addAttribute("pageTitle", "Đăng Nhập");
		return "security/login";
	}
	
	@RequestMapping("/security/login/success")
	public String loginSuccess(Model model) {
		model.addAttribute("message","Đăng nhập thành công");
		return "redirect:/product/list";
	}
	
	@RequestMapping("/security/login/error")
	public String loginError(Model model) {
		model.addAttribute("message","Sai thông tin đăng nhập!");
		model.addAttribute("pageTitle", "Đăng Nhập");
		return "security/login";
	}
	
	@RequestMapping("/security/unauthoried")
	public String unauthoried(Model model) {
		model.addAttribute("message","Không có quyền truy vấn~");
		model.addAttribute("pageTitle", "Đăng Nhập");
		return "security/login";
	}
	
	@RequestMapping("/security/logoff/success")
	public String logoffSuccess(Model model) {
		model.addAttribute("message","Bạn đã đăng xuất");
		model.addAttribute("pageTitle","Sport - DiscoverYOU");
		return "layout/body.html";
	}
	@RequestMapping("/register")
	public String showRegistrationForm(Model model) {
	    model.addAttribute("user", new Account());
	    model.addAttribute("pageTitle","Đăng Ký");
	    return "security/register";
	}
	
	@RequestMapping("/repass")
	public String repassword(Model model) {
		model.addAttribute("pageTitle","Đăng Ký");
	    return "security/repassword";
	}
	@RequestMapping("/up")
    public String update(Model model) { // Thêm Model model vào tham số
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Lấy thông tin người dùng từ cơ sở dữ liệu dựa vào tên đăng nhập
        Account user = accountService.findByUsername(username);
        
        // Thêm đối tượng người dùng vào model để hiển thị trên form
        model.addAttribute("user", user);
        model.addAttribute("pageTitle","Cập Nhật Thông Tin");
        return "security/update";
    }
	
	@RequestMapping("/oauth2/login/success")
	public String success(OAuth2AuthenticationToken token,Model model) {
	    accountService.processOAuth2Login(token);
	    model.addAttribute("pageTitle","Danh Sách Sản Phẩm");
	    return "redirect:/product/list"; 
	}
	

}
