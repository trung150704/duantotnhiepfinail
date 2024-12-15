package com.mt.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@Entity
@Table(name = "Sizes")
public class Size implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
    
    private String Id;

    private int Size;

    @OneToMany(mappedBy = "size",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SizeProduct> sizeProducts;
}
