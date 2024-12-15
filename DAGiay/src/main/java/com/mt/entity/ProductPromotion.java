package com.mt.entity;

import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@Entity
@Table(name = "ProductPromotions")
public class ProductPromotion implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
 
    private String id;
    @Temporal(TemporalType.DATE)
    @Column(name = "Created_at")
    private Date created_at = new Date();
                                                         

    @ManyToOne
    @JoinColumn(name = "Promotionid")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "Productid")
    private Product product;

    // Getters and Setters
}
