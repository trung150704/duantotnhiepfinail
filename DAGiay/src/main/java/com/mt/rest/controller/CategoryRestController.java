package com.mt.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mt.entity.Category;

import com.mt.repository.CategoryRepository;
import com.mt.service.CategoryService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/categories")
public class CategoryRestController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	CategoryRepository categoryRepository;
	
	@GetMapping()
	public List<Category> getAll(){
		return categoryService.findAll();
	}
	  @PostMapping
	  public ResponseEntity<Category> createCategory(@RequestBody Category category) {
		    try {
		        Category savedCategory = categoryService.create(category);
		        return ResponseEntity.ok(savedCategory);
		    } catch (RuntimeException e) {
		        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		    }
		}

	    // Cập nhật sản phẩm
	    @PutMapping("/{id}")
	    public Category update(@PathVariable("id") String id, @RequestBody Category category) {
	        // Cần thiết lập mã sản phẩm cho đối tượng sản phẩm mới
	        category.setId(id); // Đảm bảo mã sản phẩm trong đối tượng
	        return categoryService.update(category);
	    }

	    // Xóa sản phẩm
	    @DeleteMapping("/{id}")
	    public void delete(@PathVariable("id") String id) {
	    	categoryService.delete(id);
	    }
	
}
