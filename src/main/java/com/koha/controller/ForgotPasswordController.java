package com.koha.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.koha.service.IUserService;
import com.koha.service.impl.UserServiceImpl;

@WebServlet(urlPatterns = {"/forgot-password"})
public class ForgotPasswordController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String account = req.getParameter("account");
        if (com.koha.util.ValidatorUtil.isEmpty(account)) {
            req.setAttribute("error", "Vui lòng nhập tên đăng nhập hoặc email đã đăng ký!");
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
            return;
        }

        account = account.trim();
        boolean sent = userService.sendForgotPasswordOtp(account);

        if (sent) {
            HttpSession session = req.getSession();
            session.setAttribute("reset_account", account);
            session.setAttribute("message", "Mã xác thực OTP đặt lại mật khẩu đã được gửi! Vui lòng kiểm tra email hoặc console.");
            resp.sendRedirect(req.getContextPath() + "/reset-password");
        } else {
            req.setAttribute("error", "Không tìm thấy tài khoản hoặc địa chỉ email này trong hệ thống!");
            req.setAttribute("account", account);
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
        }
    }
}
