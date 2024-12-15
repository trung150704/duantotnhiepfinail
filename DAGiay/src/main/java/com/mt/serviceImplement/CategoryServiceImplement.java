package com.mt.serviceImplement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mt.entity.Category;
import com.mt.repository.CategoryRepository;
import com.mt.service.CategoryService;

@Service
public class CategoryServiceImplement implements CategoryService{
	@Autowired
	CategoryRepository categoryDAO;

	@Override
	public List<Category> findAll() {
		return categoryDAO.findAll();
	}

	 @Override
	    public Category create(Category category) {
	        // Kiểm tra trùng lặp ID hoặc Name
	        if (categoryDAO.existsById(category.getId())) {
	            throw new RuntimeException("Category ID already exists!");
	        }
	        
	        if (categoryDAO.existsByName(category.getName())) {
	            throw new RuntimeException("Category Name already exists!");
	        }

	        return categoryDAO.save(category); // Tạo mới Category
	    }

	@Override
	public Category update(Category category) {
		
		return categoryDAO.save(category);
	}

	@Override
	public void delete(String id) {
		categoryDAO.deleteById(id);
		
	}

	@Override
	public List<Category> getAllCategories() {
		 return categoryDAO.findAllCategories();
	}

	
	
}
