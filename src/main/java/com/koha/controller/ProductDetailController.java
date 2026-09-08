package com.koha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.koha.entity.Product;
import com.koha.service.IProductService;

@Controller
public class ProductDetailController {

    @Autowired
    private IProductService productService;

    @GetMapping("/product/detail")
    public String productDetail(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id == null) {
            return "redirect:/product";
        }

        Product product = productService.findById(id);
        if (product == null) {
            model.addAttribute("message", "Sản phẩm không tồn tại hoặc đã ngừng kinh doanh!");
            return "error";
        }

        // Lấy thêm các sản phẩm cùng danh mục để hiển thị phần Gợi ý liên quan
        List<Product> relatedProducts = null;
        if (product.getCategory() != null) {
            relatedProducts = productService.findByCategoryId(product.getCategory().getCategoryId());
        }

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);

        return "product-detail";
    }
}


