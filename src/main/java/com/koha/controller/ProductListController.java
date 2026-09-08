package com.koha.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.koha.entity.Category;
import com.koha.entity.Product;
import com.koha.service.ICategoryService;
import com.koha.service.IProductService;
import com.koha.service.impl.CategoryServiceImpl;
import com.koha.service.impl.ProductServiceImpl;

@WebServlet(urlPatterns = {"/product", "/products"})
public class ProductListController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IProductService productService = new ProductServiceImpl();
    private ICategoryService categoryService = new CategoryServiceImpl();

    private static final int PAGE_SIZE = 6; // Yêu cầu: Phân trang 6 sản phẩm / trang

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageRaw = req.getParameter("page");
        String categoryIdRaw = req.getParameter("categoryId");
        String keyword = req.getParameter("keyword");

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
        }

        List<Product> listProduct;
        int totalProducts;

        if (categoryIdRaw != null && !categoryIdRaw.trim().isEmpty()) {
            int cateId = Integer.parseInt(categoryIdRaw.trim());
            listProduct = productService.findByCategoryId(cateId);
            totalProducts = listProduct.size();
            req.setAttribute("selectedCategoryId", cateId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            listProduct = productService.searchByName(keyword.trim());
            totalProducts = listProduct.size();
            req.setAttribute("keyword", keyword.trim());
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

        req.getRequestDispatcher("/views/product-list.jsp").forward(req, resp);
    }
}
