package com.mt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mt.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	@Query(value = "SELECT * FROM orderdetails od WHERE od.orderid = :orderId", nativeQuery = true)
	List<OrderDetail> findByOrderId(@Param("orderId") Integer orderId);
}
