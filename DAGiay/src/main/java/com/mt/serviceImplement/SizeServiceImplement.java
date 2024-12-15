package com.mt.serviceImplement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mt.entity.Product;
import com.mt.entity.Size;
import com.mt.repository.ProductRepository;
import com.mt.repository.SizeRepository;
import com.mt.service.SizeService;

@Service
public class SizeServiceImplement implements SizeService {

    @Autowired
    SizeRepository sizeRepository;

	@Override
	public List<Size> findAll() {
		return sizeRepository.findAll();
	}

    
}
