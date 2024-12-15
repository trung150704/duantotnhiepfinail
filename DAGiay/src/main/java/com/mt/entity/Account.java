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
@Table(name = "Accounts")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
    String username;
    String password;
    String fullname;
    boolean role;
    String email;
    String address;
    @Transient
    String confirmPassword;

    @Temporal(TemporalType.DATE)
    @Column(name = "Created_at")
    private Date created_at = new Date();

    @OneToMany(mappedBy = "account")
    @JsonIgnoreProperties("account")
    private List<Order> orders;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    List<FavoriteProduct> favoriteProducts;
}
