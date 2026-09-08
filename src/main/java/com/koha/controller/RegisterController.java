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

@WebServlet(urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        java.util.Map<String, String> errors = new java.util.HashMap<>();

        // 1. Kiểm tra validation từng trường bằng ValidatorUtil
        if (com.koha.util.ValidatorUtil.isEmpty(username)) {
            errors.put("username", "Tên đăng nhập không được để trống!");
        } else if (!com.koha.util.ValidatorUtil.isValidUsername(username)) {
            errors.put("username", "Tên đăng nhập từ 3 đến 50 ký tự (chữ cái, chữ số, gạch dưới)!");
        } else if (userService.checkExistUsername(username.trim())) {
            errors.put("username", "Tên đăng nhập '" + username.trim() + "' đã có người sử dụng!");
        }

        if (com.koha.util.ValidatorUtil.isEmpty(fullname)) {
            errors.put("fullname", "Họ và tên không được để trống!");
        } else if (!com.koha.util.ValidatorUtil.isValidLength(fullname, 2, 100)) {
            errors.put("fullname", "Họ và tên từ 2 đến 100 ký tự!");
        }

        if (com.koha.util.ValidatorUtil.isEmpty(email)) {
            errors.put("email", "Email không được để trống để nhận mã OTP!");
        } else if (!com.koha.util.ValidatorUtil.isValidEmail(email)) {
            errors.put("email", "Địa chỉ email không đúng định dạng chuẩn (ví dụ: user@example.com)!");
        } else if (userService.checkExistEmail(email.trim())) {
            errors.put("email", "Email '" + email.trim() + "' đã được đăng ký trên hệ thống!");
        }

        if (com.koha.util.ValidatorUtil.isNotEmpty(phone) && !com.koha.util.ValidatorUtil.isValidPhone(phone)) {
            errors.put("phone", "Số điện thoại không hợp lệ (cần 10 số, bắt đầu bằng 03, 05, 07, 08, 09)!");
        }

        if (com.koha.util.ValidatorUtil.isEmpty(password)) {
            errors.put("password", "Mật khẩu không được để trống!");
        } else if (!com.koha.util.ValidatorUtil.isValidPassword(password)) {
            errors.put("password", "Mật khẩu phải có tối thiểu 6 ký tự!");
        }

        if (com.koha.util.ValidatorUtil.isEmpty(confirmPassword)) {
            errors.put("confirmPassword", "Vui lòng nhập lại mật khẩu xác nhận!");
        } else if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "Mật khẩu xác nhận không khớp với mật khẩu!");
        }

        // Nếu có lỗi, giữ lại thông tin đã nhập và quay lại trang đăng ký
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("oldUsername", username);
            req.setAttribute("oldFullname", fullname);
            req.setAttribute("oldEmail", email);
            req.setAttribute("oldPhone", phone);
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        username = username.trim();
        email = email.trim();
        fullname = fullname.trim();
        if (phone != null) phone = phone.trim();

        // 2. Tiến hành đăng ký và gửi mã OTP qua email
        boolean success = userService.registerWithOtp(username, password.trim(), fullname, email, phone);

        if (success) {
            HttpSession session = req.getSession();
            session.setAttribute("otp_username", username);
            session.setAttribute("otp_email", email);
            session.setAttribute("message", "Mã xác thực OTP đã được gửi đến email " + email + ". Vui lòng kiểm tra hộp thư (hoặc Console) để kích hoạt tài khoản!");

            resp.sendRedirect(req.getContextPath() + "/verify-otp");
        } else {
            req.setAttribute("error", "Đăng ký thất bại, vui lòng thử lại sau!");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
        }
    }
}
