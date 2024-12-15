package com.mt.serviceImplement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mt.entity.Payment;
import com.mt.repository.PaymentRepository;
import com.mt.service.PaymentService;

@Service
public class PaymentServiceImplement implements PaymentService{
	
	@Autowired
	PaymentRepository paymentRepository;
	
	@Override
	public List<Payment> findAll() {
		return paymentRepository.findAll();
	}

	@Override
	public Payment findById(Integer id) {
		return paymentRepository.findById(id).orElse(null);
	}

}
