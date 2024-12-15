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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mt.entity.Account;
import com.mt.entity.Order;
import com.mt.entity.Size;
import com.mt.service.AccountService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/account1")
public class AccountRestController1 {
	@Autowired
	AccountService accountService;

	@GetMapping
	public List<Account> getAll() {
		return accountService.findAll();
	}
	@PostMapping
	public Account create(@RequestBody Account account) {
		return accountService.create(account);
	}
	@DeleteMapping("/{username}")
	public ResponseEntity<?> deleteByUsername(@PathVariable("username") String username) {
	    boolean isDeleted = accountService.deleteByUsername(username);
	    if (isDeleted) {
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
	}
	@PutMapping("/{id}")
	public Account update(@PathVariable("id") String id, @RequestBody Account account) {
		account.setUsername(id);
		return accountService.update(account);
	}
}
