package com.mt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mt.entity.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE o.account.username = :username")
	List<Order> findByUsername(@Param("username") String username);

	@Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails WHERE o.id = :orderId")
	Order findByIdWithItems(@Param("orderId") Integer orderId);

	Optional<Order> findById(Integer id);
}
