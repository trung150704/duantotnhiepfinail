package com.mt.serviceImplement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mt.entity.Order;
import com.mt.entity.OrderDetail;
import com.mt.entity.OrderDetailDTO;
import com.mt.repository.OrderDetailRepository;
import com.mt.repository.OrderRepository;
import com.mt.repository.ProductRepository;
import com.mt.service.OrderDetailService;
import com.mt.service.OrderService;
import com.mt.service.ProductService;

@Service
public class OrderDetailServiceImplement implements OrderDetailService {

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    ProductRepository productRepository;
    @Override
    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    @Override
    public OrderDetail findById(Integer id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Integer orderId, List<OrderDetail> orderDetails) {
        // Lấy Order từ database
        Order order = orderService.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }

        for (OrderDetail detail : orderDetails) {
            detail.setOrder(order); // Gắn Order vào OrderDetail
            detail.setProduct(productService.findById(detail.getProduct().getId())); // Lấy Product từ ID
            orderDetailRepository.save(detail); // Lưu OrderDetail vào database
        }
    }

    @Override
    public OrderDetail save(OrderDetail orderDetail) { // Đổi tên thành save
        return orderDetailRepository.save(orderDetail);
    }
    
    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
    
    @Override
    public void saveOrderDetails(Integer orderId, List<OrderDetailDTO> orderDetails) {
        // Lấy order từ database
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Order ID"));

        // Lặp qua danh sách OrderDetailDTO
        for (OrderDetailDTO dto : orderDetails) {
            OrderDetail orderDetail = new OrderDetail();

            // Gán thông tin vào OrderDetail
            orderDetail.setOrder(order);
            orderDetail.setProduct(productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Product ID")));
            orderDetail.setQuantity(dto.getQuantity());
            orderDetail.setPrice(dto.getPrice());

            // Lưu vào database
            orderDetailRepository.save(orderDetail);
        }
    }

	@Override
	public List<OrderDetail> saveAll(List<OrderDetail> orderDetails) {
		return orderDetailRepository.saveAll(orderDetails);
	}
}
