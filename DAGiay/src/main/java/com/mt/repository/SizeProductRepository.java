package com.mt.repository;

import org.springframework.stereotype.Repository;

import com.mt.entity.Product;
import com.mt.entity.SizeProduct;
import com.mt.entity.SizeProductKey;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface SizeProductRepository extends JpaRepository<SizeProduct, SizeProductKey> {

	Optional<SizeProduct> findById(SizeProductKey key);

	@Query(value = "SELECT COUNT FROM SizeProduct WHERE SizeId = :sizeId AND ProductId = :productId", nativeQuery = true)
	Integer findCountByProductIdAndSizeId(@Param("productId") Integer productId, @Param("sizeId") String sizeId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SizeProduct SET Count = :count WHERE SizeId = :sizeId AND ProductId = :productId", nativeQuery = true)
	int updateProductCount(@Param("sizeId") String sizeId, @Param("productId") Integer productId, @Param("count") Integer count);

}
