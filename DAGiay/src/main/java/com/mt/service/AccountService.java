package com.mt.service;

import java.util.List;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.mt.entity.Account;
import com.mt.entity.Order;



public interface AccountService {
	
	public Account findByUsername(String username);
	void processOAuth2Login(OAuth2AuthenticationToken token);
	Account create(Account user);
	Account updateAccount(Account account);
	public List<Account> getAdministrators();

	public List<Account> findAll();
	boolean changePassword(String username, String oldPassword, String newPassword);
	boolean deleteByUsername(String username);
	boolean delete(String id);
	Account update(Account account);
	boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
