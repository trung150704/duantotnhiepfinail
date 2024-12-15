package com.mt.service;

import java.util.List;
import com.mt.entity.OrderDetail;
import com.mt.entity.OrderDetailDTO;

public interface OrderDetailService {
    List<OrderDetail> findAll();
    OrderDetail findById(Integer id);
    void save(Integer orderId, List<OrderDetail> orderDetails);
    OrderDetail save(OrderDetail orderDetail);
    List<OrderDetail> getOrderDetailsByOrderId(Integer orderId);
	void saveOrderDetails(Integer orderId, List<OrderDetailDTO> orderDetails);
	List<OrderDetail> saveAll(List<OrderDetail> orderDetails);
}
