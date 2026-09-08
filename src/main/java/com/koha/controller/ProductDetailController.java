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

import com.koha.entity.Product;
import com.koha.service.IProductService;
import com.koha.service.impl.ProductServiceImpl;

@WebServlet(urlPatterns = {"/product/detail"})
public class ProductDetailController extends HttpServlet {
@Controller
public class ProductDetailController {

    private static final long serialVersionUID = 1L;
    private IProductService productService = new ProductServiceImpl();
    @Autowired
    private IProductService productService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idRaw = req.getParameter("id");
    @GetMapping("/product/detail")
    public String productDetail(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id == null) {
            return "redirect:/product";
        }

        if (idRaw == null || idRaw.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/product");
            return;
        Product product = productService.findById(id);
        if (product == null) {
            model.addAttribute("message", "Sản phẩm không tồn tại hoặc đã ngừng kinh doanh!");
            return "error";
        }

        try {
            int productId = Integer.parseInt(idRaw.trim());
            Product product = productService.findById(productId);
        // Lấy thêm các sản phẩm cùng danh mục để hiển thị phần Gợi ý liên quan
        List<Product> relatedProducts = null;
        if (product.getCategory() != null) {
            relatedProducts = productService.findByCategoryId(product.getCategory().getCategoryId());
        }

            if (product == null) {
                req.setAttribute("message", "Sản phẩm không tồn tại hoặc đã ngừng kinh doanh!");
                req.getRequestDispatcher("/views/error.jsp").forward(req, resp);
                return;
            }
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);

            // Lấy thêm các sản phẩm cùng danh mục để hiển thị phần Gợi ý liên quan
            List<Product> relatedProducts = null;
            if (product.getCategory() != null) {
                relatedProducts = productService.findByCategoryId(product.getCategory().getCategoryId());
            }

            req.setAttribute("product", product);
            req.setAttribute("relatedProducts", relatedProducts);
            req.getRequestDispatcher("/views/product-detail.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/product");
        }
        return "product-detail";
    }
}

