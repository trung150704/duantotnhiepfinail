package com.mt.entity;

import java.util.List;

public class OrderRequest {
	private List<OrderDetail> orderDetails;

    // Getter và setter
    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
