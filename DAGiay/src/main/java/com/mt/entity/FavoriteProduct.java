package com.mt.entity;

import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
@Data
@Entity
@Table(name = "FavoriteProducts")
public class FavoriteProduct implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
   
    private String id;

    @ManyToOne
    @JoinColumn(name = "Accountid")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "Productid")
    private Product product;

    // Getters and Setters
}
