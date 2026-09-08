package com.koha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.koha.entity.Category;
import com.koha.entity.Product;
import com.koha.service.ICategoryService;
import com.koha.service.IProductService;

@Controller
public class ProductListController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    private static final int PAGE_SIZE = 6; // Yêu cầu: Phân trang 6 sản phẩm / trang

    @GetMapping({"/product", "/products"})
    public String listProducts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        if (page < 1) {
            page = 1;
        }

        List<Product> listProduct;
        int totalProducts;

        if (categoryId != null && categoryId > 0) {
            listProduct = productService.findByCategoryId(categoryId);
            totalProducts = listProduct.size();
            model.addAttribute("selectedCategoryId", categoryId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            listProduct = productService.searchByName(keyword.trim());
            totalProducts = listProduct.size();
            model.addAttribute("keyword", keyword.trim());
        } else {
            totalProducts = productService.count();
            listProduct = productService.findAll(page - 1, PAGE_SIZE);
        }

        int totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);
        if (totalPages == 0) {
            totalPages = 1;
        }

        List<Category> listCategory = categoryService.findAll();

        model.addAttribute("listProduct", listProduct);
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalProducts", totalProducts);

        return "product-list";
    }
}
