package com.koha.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.koha.entity.Category;
import com.koha.entity.Product;
import com.koha.entity.User;
import com.koha.service.ICategoryService;
import com.koha.service.IProductService;
import com.koha.service.impl.CategoryServiceImpl;
import com.koha.service.impl.ProductServiceImpl;

@WebServlet(urlPatterns = {"", "/", "/home", "/waiting", "/admin/home"})
public class HomeController extends HttpServlet {
import jakarta.servlet.http.HttpSession;

    private static final long serialVersionUID = 1L;
    private IProductService productService = new ProductServiceImpl();
    private ICategoryService categoryService = new CategoryServiceImpl();
@Controller
public class HomeController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        HttpSession session = req.getSession(false);
    @Autowired
    private IProductService productService;

        if (url.contains("/waiting")) {
            if (session != null && (session.getAttribute("user") != null || session.getAttribute("account") != null)) {
                User u = (User) (session.getAttribute("user") != null ? session.getAttribute("user") : session.getAttribute("account"));
                req.setAttribute("username", u.getUsername());
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
                    resp.sendRedirect(req.getContextPath() + "/admin/products");
                    return "redirect:/admin/categories";
                } else {
                    resp.sendRedirect(req.getContextPath() + "/home");
                    return "redirect:/home";
                }
            } else {
                resp.sendRedirect(req.getContextPath() + "/login");
            }
            return;
        }
        return "redirect:/login";
    }

        // Lấy 10 sản phẩm mới nhất hiển thị lên trang chủ theo đúng yêu cầu đề bài
        List<Product> top10Products = productService.findTop10();
        List<Category> listCategory = categoryService.findAll();

        req.setAttribute("topProducts", top10Products);
        req.setAttribute("listCategory", listCategory);

        req.getRequestDispatcher("/views/index.jsp").forward(req, resp);
    @GetMapping("/admin/home")
    public String adminHome() {
        return "redirect:/admin/categories";
    }
}

