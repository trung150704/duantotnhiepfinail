package com.mt.entity;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
@Entity
@Table(name = "Products")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
    private String name;
    private Double price;
    private String describe;
    private String images;
    @Temporal(TemporalType.DATE)
    @Column(name = "Created_at")
    private Date create_at = new Date();

    @ManyToOne
    @JoinColumn(name = "Categoryid")
    @JsonIgnoreProperties("products")
    private Category category;
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductPromotion> productPromotions;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<FavoriteProduct> favoriteProducts;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<SizeProduct> sizeProducts;
    @Column(name = "Is_Sale")
    private Integer is_sale;
    @Column(name = "Discount")
    private Integer discount;
    // Getters and Setters
}
