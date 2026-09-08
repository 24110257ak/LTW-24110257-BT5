package com.koha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.koha.entity.Category;
import com.koha.entity.Product;
import com.koha.entity.User;
import com.koha.service.ICategoryService;
import com.koha.service.IProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping({"", "/", "/home"})
    public String home(Model model) {
        // Lấy 10 sản phẩm mới nhất hiển thị lên trang chủ
        List<Product> top10Products = productService.findTop10();
        List<Category> listCategory = categoryService.findAll();

        model.addAttribute("topProducts", top10Products);
        model.addAttribute("listCategory", listCategory);

        return "index";
    }

    @GetMapping("/waiting")
    public String waiting(HttpSession session) {
        if (session != null) {
            User u = (User) (session.getAttribute("user") != null ? session.getAttribute("user") : session.getAttribute("account"));
            if (u != null) {
                if (u.getRoleid() == 1) {
                    return "redirect:/admin/categories";
                } else {
                    return "redirect:/home";
                }
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "redirect:/admin/categories";
    }
}

