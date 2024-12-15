package com.mt.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mt.entity.Account;

import com.mt.service.AccountService;

@CrossOrigin("*")
@Controller

public class AccountRestController {
	@Autowired
	AccountService accountService;
	@Autowired
	BCryptPasswordEncoder pe;

	@GetMapping("/rest/accounts")
	public List<Account> getAccounts(@RequestParam("admin") Optional<Boolean> isAdmin) {
		// Kiểm tra giá trị của admin và trả về danh sách phù hợp
		if (isAdmin.orElse(false)) {
			return accountService.getAdministrators(); // Trả về danh sách admin nếu isAdmin là true
		}
		return accountService.findAll(); // Trả về tất cả tài khoản nếu isAdmin là false hoặc không được chỉ định
	}

	@PostMapping("/register")
	public String registerUser(Account user, RedirectAttributes redirectAttributes) {
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu xác nhận không khớp");
			return "redirect:/register";
		}
		if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Username không được để trống");
			return "redirect:/register";
		}
		if (accountService.existsByUsername(user.getUsername())) {
			redirectAttributes.addFlashAttribute("errorMessage", "Username đã tồn tại");
			return "redirect:/register";
		}
		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Email không được để trống");
			return "redirect:/register";
		}

		if (accountService.existsByEmail(user.getEmail())) {
			redirectAttributes.addFlashAttribute("errorMessage", "Email đã tồn tại");
			return "redirect:/register";
		}
		if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu không được để trống");
			return "redirect:/register";
		}
		if (user.getConfirmPassword() == null || user.getConfirmPassword().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng xác nhận mật khẩu");
			return "redirect:/register";
		}
		if (user.getFullname() == null || user.getFullname().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Họ tên không được để trống");
			return "redirect:/register";
		}
		if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Địa chỉ không được để trống");
			return "redirect:/register";
		}
		if (!user.isRole()) {
			user.setRole(false);
			accountService.create(user);
			redirectAttributes.addFlashAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
		}

		System.out.println("Redirecting to /security/login/form");
		return "redirect:/security/login/form";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String username, @RequestParam String oldPassword,
			@RequestParam String newPassword, @RequestParam String confirmPassword,
			RedirectAttributes redirectAttributes) {
		if (username == null || username.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Username không được để trống");
			return "redirect:/repass";
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.getName().equals(username)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền thực hiện thao tác này");
			return "redirect:/repass";
		}

		if (oldPassword == null || oldPassword.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu cũ không được để trống");
			return "redirect:/repass";
		}
		if (newPassword == null || newPassword.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới không được để trống");
			return "redirect:/repass";
		}
		if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Xác nhận mật khẩu không được để trống");
			return "redirect:/repass";
		}
		if (!newPassword.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới và xác nhận không khớp!");
			return "redirect:/repass";
		}

		boolean result = accountService.changePassword(username, oldPassword, newPassword);
		if (result) {
			redirectAttributes.addFlashAttribute("message", "Đổi mật khẩu thành công!");
			return "redirect:/product/list";
		} else {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Đổi mật khẩu thất bại! Kiểm tra mật khẩu cũ và thử lại.");
			return "redirect:/repass";
		}
	}

	@PostMapping("/update")
	public String updateAccount(@ModelAttribute Account account, RedirectAttributes redirectAttributes) {

		if (account.getEmail() == null || account.getEmail().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Email không được để trống");
			return "redirect:/up";
		}
		if (account.getFullname() == null || account.getFullname().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Họ tên không được để trống");
			return "redirect:/up";
		}
		if (account.getAddress() == null || account.getAddress().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Địa chỉ không được để trống");
			return "redirect:/up";
		}
		Account updatedAccount = accountService.updateAccount(account);
		if (updatedAccount != null) {
			redirectAttributes.addFlashAttribute("message", "Thông tin đã được cập nhật thành công!");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Không thể cập nhật thông tin!");
		}
		return "redirect:/product/list";
	}

	@PostMapping
	public void loginFromOAuth2(OAuth2AuthenticationToken oauth2) {
		// String fullname = oauth2.getPrincipal().getAtribute("name");
		String email = oauth2.getPrincipal().getAttribute("email");
		String password = Long.toHexString(System.currentTimeMillis());

		UserDetails user = User.withUsername(email).password(pe.encode(password)).roles("cust").build();
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@GetMapping("/current")
	public ResponseEntity<String> getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getName())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
		}
		String username = authentication.getName();
		return ResponseEntity.ok(username);
	}
}