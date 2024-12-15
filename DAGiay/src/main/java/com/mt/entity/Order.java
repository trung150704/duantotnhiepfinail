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
@Table(name = "Orders")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)	
    @Column(name = "Created_at", nullable = false)
    private Date createdAt = new Date();

    @Column(name = "HoTen", nullable = false)
    private String hoten;

    @Column(name = "SDT", nullable = false)
    private String sdt;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "Status", nullable = false)
    private Boolean status = false;

    @Column(name = "TotalAmount", nullable = false)
    private Double totalamount;

    // Quan hệ với bảng Account
    @ManyToOne
    @JoinColumn(name = "AccountId", referencedColumnName = "username") // username từ bảng Account
    @JsonIgnoreProperties("orders")
    private Account account;

    // Quan hệ với bảng Payment
    @ManyToOne
    @JoinColumn(name = "PaymentId", referencedColumnName = "id") // id từ bảng Payment
    private Payment payment;

    // Quan hệ với bảng OrderDetail
    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
