package com.mt.entity;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;
@Data
public class SizeProductKey implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer productId; 
    private String sizeId; 

    // Constructor mặc định
    public SizeProductKey() {}

    public SizeProductKey(Integer productId, String sizeId) {
        this.productId = productId;
        this.sizeId = sizeId;
    }


    // Phương thức equals và hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SizeProductKey that = (SizeProductKey) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(sizeId, that.sizeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, sizeId);
    }
}
