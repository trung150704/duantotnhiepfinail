package com.mt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mt.entity.Category;


public interface CategoryRepository extends JpaRepository<Category, String>{
	   @Query("SELECT c FROM Category c")
	    List<Category> findAllCategories();
	 // Kiểm tra sự tồn tại của Category theo tên
    boolean existsByName(String name);
    
    // Tìm Category theo tên (nếu cần dùng)
    Optional<Category> findByName(String name);
}
