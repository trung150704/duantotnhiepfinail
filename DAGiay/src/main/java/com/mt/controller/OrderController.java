package com.mt.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mt.entity.Order;
import com.mt.entity.OrderDetail;
import com.mt.service.AccountService;
import com.mt.service.OrderDetailService;
import com.mt.service.OrderService;
import com.mt.service.PaymentService;
import com.mt.service.ProductService;
import java.util.Locale;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	PaymentService paymentService;

	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	AccountService accountService;

	@Autowired
	ProductService productService;

	@RequestMapping("/order/checkout")
	public String checkout(Model model) {
		model.addAttribute("payments", paymentService.findAll());
		model.addAttribute("pageTitle", "Thanh Toán");
		return "order/checkout";
	}

	@GetMapping("/order/success")
	public String confirmOrder(@RequestParam("orderId") Integer orderId, Model model) {
		Order order = orderService.findById(orderId);
		
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
	    String formattedTotalAmount = currencyFormat.format(order.getTotalamount());

	    formattedTotalAmount = formattedTotalAmount.replace("₫", "VNĐ");

		model.addAttribute("order", order);
		model.addAttribute("pageTitle", "Thanh Toán Thành Công");
		model.addAttribute("formattedTotalAmount", formattedTotalAmount);
		model.addAttribute("orderId", orderId);
		return "order/success";
	}

	@RequestMapping("/order/qrcode")
	public String qrcode(Model model) {
		model.addAttribute("pageTitle", "Thanh Toán Mã QR");
		String vnpayUrl = "/Images/qrcode.jpg";
		model.addAttribute("vnpayUrl", vnpayUrl);
		return "order/qrcode";
	}

	@RequestMapping("/your_orders/{orderId}")
	public String viewOrderDetails(@PathVariable Integer orderId, Model model) {
		Order order = orderService.findById(orderId);

		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
		model.addAttribute("order", order);
		model.addAttribute("orderDetails", orderDetails);
		model.addAttribute("pageTitle", "Chi Tiết Đơn Hàng");
		return "order/order_detail";
	}

	@RequestMapping("/your_orders")
	public String list(Model model, HttpServletRequest request) {
		String username = request.getRemoteUser();
		model.addAttribute("orders", orderService.findByUsername(username));
		model.addAttribute("pageTitle", "Đơn Hàng");
		return "order/your_orders";
	}
}
