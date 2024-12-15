package com.mt.service;

import java.util.List;

import com.mt.entity.Category;

public interface CategoryService {

	List<Category> findAll();
	 Category create(Category category);

	 Category update(Category category);

	 void delete(String id); // Chỉnh sửa kiểu tham số thành String
	 List<Category> getAllCategories();
}
