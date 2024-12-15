package com.mt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.mt.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer > {
	@Query("SELECT p FROM Product p WHERE p.category.id = ?1")
	List<Product> findByCategoryId(String cid);
	 Page<Product> findByDescribe(String describe, Pageable pageable);
	 @Query("SELECT DISTINCT p.describe FROM Product p")
	   List<String> findDistinctProductLines();
	 
	Page<Product> findByCategoryId(String cid, Pageable pageable);

	Page<Product> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);

	List<Product> findByNameContainingIgnoreCase(String name);
	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
	// ProductRepository.java
	List<Product> findByDescribeContainingIgnoreCase(String describe);
	@Query("SELECT p FROM Product p WHERE p.is_sale = 1")
	List<Product> findAllSaleProducts();
}
