package com.mt.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.mt.entity.Order;
import com.mt.entity.OrderDetail;


public interface OrderService {
    Order create(JsonNode orderData);
    List<Order> findByUsername(String username);

    List<Order> findAll();
	
	Order findById(Integer id);
	Order update(Order order);
	void delete(Integer id);
	Order create(Order order);

}
