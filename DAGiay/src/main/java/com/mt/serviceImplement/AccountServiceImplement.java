package com.mt.serviceImplement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.mt.entity.Account;
import com.mt.entity.Order;
import com.mt.repository.AccountRepository;
import com.mt.service.AccountService;



@Service
public class AccountServiceImplement implements AccountService{
	@Autowired
	AccountRepository accountRepository;
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	@Override
	public Account findByUsername(String username) {
		Account account = accountRepository.findByUsername(username).get();
		return account;
	}

	@Override
	public List<Account> getAdministrators() {
		return accountRepository.getAdministrators();
	}

	@Override
	public List<Account> findAll() {
		return accountRepository.findAll();
	}
	@Override
    public Account create(Account user) {
		user.setPassword(passwordEncoder.encode(user.getPassword())); // Mã hóa mật khẩu
        return accountRepository.save(user);
    }
	@Override
	public boolean changePassword(String username, String oldPassword, String newPassword) {
	    Account account = accountRepository.findByUsername(username).get();
	    if (account != null && passwordEncoder.matches(oldPassword, account.getPassword())) {
	        account.setPassword(passwordEncoder.encode(newPassword)); // Mã hóa mật khẩu mới
	        accountRepository.save(account);
	        return true;
	    }
	    return false;
	}
	@Override
    public Account updateAccount(Account updatedAccount) {
        Account existingAccount = accountRepository.findByUsername(updatedAccount.getUsername()).orElse(null);
        if (existingAccount != null) {
            // Giữ nguyên mật khẩu hiện tại
            updatedAccount.setPassword(existingAccount.getPassword());
            return accountRepository.save(updatedAccount);
        }
        return null;
    }
	@Override
    public void processOAuth2Login(OAuth2AuthenticationToken token) {
        String email = token.getPrincipal().getAttribute("email");
        if (accountRepository.findByUsername(email).isEmpty()) {
            Account newUser = new Account();
            newUser.setUsername(email);
            newUser.setPassword(passwordEncoder.encode("default")); // Hoặc sử dụng một mật khẩu ngẫu nhiên
            newUser.setRole(false); // Vai trò mặc định là USER
            accountRepository.save(newUser);
        }
        loginFromOAuth2(token);
    }

    private void loginFromOAuth2(OAuth2AuthenticationToken token) {
        String email = token.getPrincipal().getAttribute("email");
        UserDetails user = User.withUsername(email)
                .password(passwordEncoder.encode("default")) // Sử dụng mật khẩu mặc định đã lưu
                .roles("USER").build();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    
    @Override
    public boolean deleteByUsername(String username) {
        // Tìm tài khoản theo username
        Account account = findByUsername(username);
        if (account != null) {
            // Xóa tài khoản nếu tồn tại
            accountRepository.delete(account);
            return true; // Trả về true nếu xóa thành công
        }
        return false; // Trả về false nếu không tìm thấy tài khoản
    }

	@Override
	public boolean delete(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account update(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public boolean existsByUsername(String username) {
	    return accountRepository.findByUsername(username).isPresent();
	}

	@Override
	public boolean existsByEmail(String email) {
	    return accountRepository.findByEmail(email).isPresent();
	}
}
