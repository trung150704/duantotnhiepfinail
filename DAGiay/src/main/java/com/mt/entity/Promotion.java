package com.mt.entity;

import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@Entity
@Table(name = "Promotions")
public class Promotion implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
   
    private String  id;

    private String code;
    private String describe;
    private Double sale;
    private String datestart;
    private String dateend;
    private Boolean ctivated;

    @OneToMany(mappedBy = "promotion")
    private List<ProductPromotion> productPromotions;

    // Getters and Setters
}
