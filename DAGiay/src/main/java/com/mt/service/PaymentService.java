package com.mt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mt.entity.Payment;


@Service
public interface PaymentService {
	List<Payment> findAll();
	Payment findById(Integer id);
}
