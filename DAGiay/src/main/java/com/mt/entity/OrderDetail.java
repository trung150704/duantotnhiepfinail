package com.mt.entity;

import javax.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

@Data
@Entity
@Table(name = "OrderDetails")
public class OrderDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "orderid")
	@JsonIgnore
	private Order order;
	@ManyToOne
	@JoinColumn(name = "productid")
	//@JsonIgnore
	private Product product;
	private Integer quantity;
	private Double price;

	
}
