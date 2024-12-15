package com.mt.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShoppingCartController {
    @RequestMapping("/cart/view")
    public String view(Model model) {
        model.addAttribute("pageTitle", "Giỏ Hàng");
        return "cart/view";
    }

    @RequestMapping("/cart/heart")
    public String heart(Model model) {
        model.addAttribute("pageTitle", "Sản Phẩm Yêu Thích");
        return "cart/heart";
    }
}
