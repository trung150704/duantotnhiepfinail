package com.mt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mt.entity.Product;
import com.mt.service.ProductService;

@Controller
public class HomeController {
	@Autowired
    private ProductService productService;

    @RequestMapping({"/", "/home/index" })
    public String home(Model model) {
        // Lấy danh sách tất cả sản phẩm
        List<Product> products = productService.findAll();
        
        // Truyền danh sách sản phẩm vào model để sử dụng trong view
        model.addAttribute("items", products);
        model.addAttribute("pageTitle","Sport - DiscoverYOU");
        return "layout/body.html";
    }
	
	@RequestMapping({"/admin","/admin/home/index"})
	public String admin() {
		return "redirect:/assets/admin/index.html";
	}
	
}
