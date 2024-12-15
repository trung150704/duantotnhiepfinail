package com.mt.serviceImplement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mt.entity.Account;
import com.mt.entity.Order;
import com.mt.entity.OrderDetail;
import com.mt.repository.AccountRepository;
import com.mt.repository.OrderDetailRepository;
import com.mt.repository.OrderRepository;
import com.mt.service.OrderService;

@Service
public class OrderServiceImplement implements OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Override
	public Order create(JsonNode orderData) {
		ObjectMapper mapper = new ObjectMapper();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (authentication == null || "anonymousUser".equals(authentication.getName())) ? null
				: authentication.getName();

		Order order = mapper.convertValue(orderData, Order.class);

		Account account;
		if (username != null) {
			account = accountRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("Account not found with username: " + username));
		} else if (order.getAccount() != null && order.getAccount().getUsername() != null) {
			account = accountRepository.save(order.getAccount());
		} else {
			throw new RuntimeException("Account information is missing.");
		}

		order.setAccount(account);
		if (username != null) {
			account = accountRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("Account not found with username: " + username));
		} else {
			account = order.getAccount();
			if (account != null && account.getUsername() == null) {
				account = accountRepository.save(account);
			} else {
				throw new RuntimeException("Account information is missing.");
			}
		}

		order.setAccount(account);

		Order savedOrder = orderRepository.save(order);

		TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {
		};
		List<OrderDetail> details = mapper.convertValue(orderData.get("orderDetails"), type).stream().peek(detail -> {
			System.out.println("Product ID: " + detail.getProduct().getId());
			detail.setOrder(savedOrder);
		}).collect(Collectors.toList());

		orderDetailRepository.saveAll(details);

		return savedOrder;
	}

	@Override
	public List<Order> findByUsername(String username) {
		return orderRepository.findByUsername(username);
	}

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Override
	public Order findById(Integer id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Override
	public Order update(Order order) {
		return orderRepository.save(order);
	}

	@Override
	public void delete(Integer id) {
		orderRepository.deleteById(id);
	}

	@Override
	public Order create(Order order) {
		return orderRepository.save(order);
	}

}
