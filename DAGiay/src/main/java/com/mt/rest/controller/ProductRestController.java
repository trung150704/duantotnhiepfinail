package com.mt.rest.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.mt.entity.Product;
import com.mt.repository.SizeProductRepository;
import com.mt.service.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/products")
public class ProductRestController {

	@Autowired
	ProductService productService;

	@Autowired
	SizeProductRepository sizeProductRepository;
	// Lấy sản phẩm theo ID
	@GetMapping("/{id}")
	public Product getOne(@PathVariable("id") Integer id) {
		return productService.findById(id);
	}

	// Lấy tất cả sản phẩm
	@GetMapping
	public List<Product> getAll() {
		return productService.findAll(); // Gọi phương thức không phân trang
	}

	// Lấy tất cả sản phẩm có phân trang
	@GetMapping("/paged")
	public Page<Product> getAllPaged(Pageable pageable) {
		return productService.findAll(pageable); // Gọi phương thức phân trang
	}

	// Tạo sản phẩm mới
	@PostMapping
	public Product create(@RequestBody Product product) {
		return productService.create(product);
	}

	// Cập nhật sản phẩm
	@PutMapping("/{id}")
	public Product update(@PathVariable("id") Integer id, @RequestBody Product product) {
		// Cần thiết lập mã sản phẩm cho đối tượng sản phẩm mới
		product.setId(id); // Đảm bảo mã sản phẩm trong đối tượng
		return productService.update(product);
	}

	// Xóa sản phẩm
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		try {
			productService.delete(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product");
		}
	}

	@GetMapping("/size/{sizeId}/product/{productId}/quantity")
	public ResponseEntity<Integer> getQuantity(@PathVariable String sizeId, @PathVariable Integer productId) {
	    Integer count = sizeProductRepository.findCountByProductIdAndSizeId(productId, sizeId);
	    if (count != null) {
	        return ResponseEntity.ok(count);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	}
	
	@PutMapping("/size/{sizeId}/product/{productId}/quantity")
	public ResponseEntity<?> updateQuantity(
			@PathVariable Integer productId,
	        @PathVariable String sizeId,
	        @RequestBody Integer newCount) {
	    System.out.println("Received data - sizeId: " + sizeId + ", productId: " + productId + ", newCount: " + newCount);

	    int rowsUpdated = sizeProductRepository.updateProductCount(sizeId,productId, newCount);
	    if (rowsUpdated > 0) {
	        return ResponseEntity.ok().body(Collections.singletonMap("success", true));
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Collections.singletonMap("success", false));
	    }
	}
}
