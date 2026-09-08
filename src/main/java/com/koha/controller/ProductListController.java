package com.koha.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.koha.entity.Category;
import com.koha.entity.Product;
import com.koha.service.ICategoryService;
import com.koha.service.IProductService;
import com.koha.service.impl.CategoryServiceImpl;
import com.koha.service.impl.ProductServiceImpl;

@WebServlet(urlPatterns = {"/product", "/products"})
public class ProductListController extends HttpServlet {
@Controller
public class ProductListController {

    private static final long serialVersionUID = 1L;
    private IProductService productService = new ProductServiceImpl();
    private ICategoryService categoryService = new CategoryServiceImpl();
    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    private static final int PAGE_SIZE = 6; // Yêu cầu: Phân trang 6 sản phẩm / trang

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageRaw = req.getParameter("page");
        String categoryIdRaw = req.getParameter("categoryId");
        String keyword = req.getParameter("keyword");
    @GetMapping({"/product", "/products"})
    public String listProducts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        int page = 1;
        if (pageRaw != null) {
            try {
                page = Integer.parseInt(pageRaw);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        if (page < 1) {
            page = 1;
        }

        List<Product> listProduct;
        int totalProducts;

        if (categoryIdRaw != null && !categoryIdRaw.trim().isEmpty()) {
            int cateId = Integer.parseInt(categoryIdRaw.trim());
            listProduct = productService.findByCategoryId(cateId);
        if (categoryId != null && categoryId > 0) {
            listProduct = productService.findByCategoryId(categoryId);
            totalProducts = listProduct.size();
            req.setAttribute("selectedCategoryId", cateId);
            model.addAttribute("selectedCategoryId", categoryId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            listProduct = productService.searchByName(keyword.trim());
            totalProducts = listProduct.size();
            req.setAttribute("keyword", keyword.trim());
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

        req.setAttribute("listProduct", listProduct);
        req.setAttribute("listCategory", listCategory);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalProducts", totalProducts);
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalProducts", totalProducts);

        req.getRequestDispatcher("/views/product-list.jsp").forward(req, resp);
        return "product-list";
    }
}

