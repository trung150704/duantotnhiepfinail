package com.mt.rest.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
public class UserRestController {

	@GetMapping
	public ResponseEntity<?> getCurrentUser(Authentication authentication) {
	    if (authentication == null || !authentication.isAuthenticated()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập");
	    }

	    // Trả về JSON thay vì chuỗi đơn giản
	    Map<String, String> response = new HashMap<>();
	    response.put("username", authentication.getName());
	    return ResponseEntity.ok(response);
	}

}

