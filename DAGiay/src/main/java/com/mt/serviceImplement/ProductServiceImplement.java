package com.mt.serviceImplement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mt.entity.Product;
import com.mt.entity.SizeProduct;
import com.mt.entity.SizeProductKey;
import com.mt.repository.ProductRepository;
import com.mt.repository.SizeProductRepository;
import com.mt.service.ProductService;

@Service
public class ProductServiceImplement implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SizeProductRepository sizeProductRepository;
    
    @Override
    public List<Product> findAll() {
        return productRepository.findAll(); // Lấy tất cả sản phẩm
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null); // Lấy sản phẩm theo mã
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable); // Phương thức phân trang
    }

    @Override
    public Page<Product> findByCategoryId(String cid, Pageable pageable) {
        return productRepository.findByCategoryId(cid, pageable); // Phân trang theo danh mục
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product); // Tạo sản phẩm mới
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product); // Cập nhật sản phẩm
    }

    @Override
    public void delete(Integer id) {
    	productRepository.deleteById(id); // Xóa sản phẩm theo mã
    }

	@Override
	public List<Product> findByCategoryId(String cid) {
		return productRepository.findByCategoryId(cid);
	}

	@Override
	public Page<Product> findByPriceRange(double minPrice, double maxPrice, Pageable pageable) {
		  return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
	}

	@Override
	public Page<Product> findByDescribe(String describe, Pageable pageable) {
		return productRepository.findByDescribe(describe, pageable);
	}

	@Override
	public List<String> findDistinctProductLines() {
		 return productRepository.findDistinctProductLines();
	}

	@Override
	public Page<Product> searchByQuery(String query, Pageable pageable) {
		 return productRepository.findByNameContainingIgnoreCase(query, pageable);
		
	}

	@Override
	public List<Product> findByDescribe(String query) {
		 return productRepository.findByDescribeContainingIgnoreCase(query);
	}

	@Override
	public Integer getAvailableQuantity(Integer productId, String sizeId) {
	    SizeProductKey key = new SizeProductKey();
	    key.setProductId(productId);
	    key.setSizeId(sizeId);

	    return sizeProductRepository.findById(key)
	                                .map(SizeProduct::getCount)
	                                .orElse(0);
	}
	
	@Override
	public List<Product> findAllSaleProducts() {
		return productRepository.findAllSaleProducts();
	}
}
