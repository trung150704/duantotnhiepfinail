package com.mt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mt.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{ 

}
