package com.mt.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mt.entity.Payment;
import com.mt.service.PaymentService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/payments")
public class PaymentRestController {
	@Autowired
	PaymentService paymentService;
	
	@GetMapping()
	public List<Payment> getAll(){
		return paymentService.findAll();
	}
}
