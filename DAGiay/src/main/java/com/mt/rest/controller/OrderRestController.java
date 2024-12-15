package com.mt.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.mt.entity.Order;
import com.mt.entity.OrderDetail;
import com.mt.entity.OrderRequest;
import com.mt.entity.Product;
import com.mt.service.OrderDetailService;
import com.mt.service.OrderService;
import com.mt.service.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {
	@Autowired
	OrderService orderService;

	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	ProductService productService;

	@GetMapping
	public List<Order> getAll() {
		return orderService.findAll();
	}

	// Tạo đơn hàng và trả về orderId
	@PostMapping
	public ResponseEntity<Integer> create(@RequestBody JsonNode orderData) {
		Order createdOrder = orderService.create(orderData);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder.getId());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable("id") Integer id) {
		Order order = orderService.findById(id);
		return ResponseEntity.ok(order);
	}

	@PutMapping("/{id}")
	public Order update(@PathVariable("id") Integer id, @RequestBody Order order) {
		order.setId(id);
		return orderService.update(order);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		try {
			orderService.delete(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product");
		}
	}
	@PostMapping("/create")
	public Order create(@RequestBody Order order) {
		return orderService.create(order);
	}
}
