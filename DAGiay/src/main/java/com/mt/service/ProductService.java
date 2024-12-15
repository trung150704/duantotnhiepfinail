package com.mt.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mt.entity.Order;
import com.mt.entity.Product;
import com.mt.entity.SizeProduct;

public interface ProductService {

    List<Product> findAll(); // Phương thức này không phân trang
    
    List<Product> findByCategoryId(String cid);
    
     Page<Product> findByPriceRange(double minPrice, double maxPrice, Pageable pageable);
    
    Page<Product> findByDescribe(String describe, Pageable pageable);
    List<String> findDistinctProductLines();
    
    Product findById(Integer id); // Chỉnh sửa kiểu tham số thành String

    Page<Product> findAll(Pageable pageable); // Phương thức phân trang
  
    Page<Product> findByCategoryId(String cid, Pageable pageable); // Phương thức phân trang theo danh mục

    Product create(Product product);

    Product update(Product product);

    void delete(Integer id); // Chỉnh sửa kiểu tham số thành String
    
    Page<Product> searchByQuery(String query, Pageable pageable);
    
	List<Product> findByDescribe(String query);

	Integer getAvailableQuantity(Integer productId,String sizeId);

	List<Product> findAllSaleProducts();

	
}
