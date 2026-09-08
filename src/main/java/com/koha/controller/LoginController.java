package com.koha.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.koha.entity.User;
import com.koha.service.IUserService;
import com.koha.service.impl.UserServiceImpl;

@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        // 1. Kiểm tra Session: Nếu đã đăng nhập thì điều hướng về trang chủ
        if (session != null && (session.getAttribute("user") != null || session.getAttribute("account") != null)) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // 2. Kiểm tra Cookie Remember Me để điền sẵn vào ô đăng nhập
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    req.setAttribute("rememberUsername", c.getValue());
                    req.setAttribute("rememberChecked", true);
                    break;
                }
            }
        }

        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String uname = req.getParameter("username");
        String pword = req.getParameter("password");
        String remember = req.getParameter("remember");

        java.util.Map<String, String> errors = new java.util.HashMap<>();

        if (com.koha.util.ValidatorUtil.isEmpty(uname)) {
            errors.put("username", "Vui lòng nhập tên đăng nhập!");
        } else if (!com.koha.util.ValidatorUtil.isValidUsername(uname)) {
            errors.put("username", "Tên đăng nhập từ 3 đến 50 ký tự (chữ cái, chữ số, gạch dưới)!");
        }

        if (com.koha.util.ValidatorUtil.isEmpty(pword)) {
            errors.put("password", "Vui lòng nhập mật khẩu!");
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("rememberUsername", uname);
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        User user = userService.login(uname.trim(), pword.trim());

        if (user != null) {
            // Kiểm tra tài khoản đã kích hoạt qua OTP chưa
            if (user.getStatus() == 0) {
                req.setAttribute("error", "Tài khoản của bạn chưa được kích hoạt qua OTP!");
                req.setAttribute("unverifiedUsername", user.getUsername());
                req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
                return;
            }

            // 1. Lưu thông tin vào Session (Đăng nhập bằng Session)
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("account", user);

            // 2. Xử lý Cookie Remember Me
            String contextPath = req.getContextPath().isEmpty() ? "/" : req.getContextPath();
            if (remember != null && ("on".equalsIgnoreCase(remember) || "true".equalsIgnoreCase(remember) || "1".equals(remember))) {
                Cookie cookie = new Cookie("username", uname.trim());
                cookie.setMaxAge(60 * 60 * 24 * 7); // Lưu 7 ngày
                cookie.setPath(contextPath);
                resp.addCookie(cookie);
            } else {
                Cookie cookie = new Cookie("username", "");
                cookie.setMaxAge(0); // Xóa cookie
                cookie.setPath(contextPath);
                resp.addCookie(cookie);
            }

            resp.sendRedirect(req.getContextPath() + "/waiting");
        } else {
            req.setAttribute("error", "Tài khoản hoặc mật khẩu không chính xác!");
            req.setAttribute("rememberUsername", uname);
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
        }
    }
}

