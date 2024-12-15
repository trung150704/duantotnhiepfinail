	package com.mt.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@IdClass(SizeProductKey.class)  // Sử dụng khóa chính tổng hợp
@Table(name = "SizeProduct")
public class SizeProduct implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "ProductId")
    private Integer productId;

    @Id
    @Column(name = "SizeId")
    private String sizeId;

    private int count;

    @ManyToOne
    @JoinColumn(name = "ProductId", insertable = false, updatable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "SizeId", insertable = false, updatable = false)
    @JsonIgnore
    private Size size;


}
