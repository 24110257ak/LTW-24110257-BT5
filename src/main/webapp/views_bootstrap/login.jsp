<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập - Bootstrap JPA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { background: linear-gradient(135deg, #0d6efd 0%, #002752 100%); min-height: 100vh; display: flex; align-items: center; justify-content: center; }
        .login-card { border-radius: 12px; border: none; box-shadow: 0 8px 24px rgba(0,0,0,0.15); width: 100%; max-width: 420px; }
    </style>
</head>
<body>
    <div class="card login-card p-4 my-4 bg-white">
        <div class="text-center mb-4">
            <h3 class="fw-bold text-primary"><i class="fa-solid fa-user-shield me-2"></i>Đăng Nhập</h3>
            <p class="text-muted small">Hệ thống quản lý web JPA 3.0 & Servlet 6.0</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2 small" role="alert">
                <i class="fa-solid fa-triangle-exclamation me-1"></i> ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="mb-3">
                <label for="username" class="form-label fw-semibold">Tài khoản (Username)</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                    <input type="text" class="form-control" id="username" name="username" value="${not empty rememberUsername ? rememberUsername : ''}" placeholder="Nhập tên đăng nhập..." required>
                </div>
            </div>

            <div class="mb-3">
                <label for="password" class="form-label fw-semibold">Mật khẩu (Password)</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Nhập mật khẩu..." required>
                </div>
            </div>

            <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" id="remember" name="remember" value="on" ${rememberChecked ? 'checked' : ''}>
                <label class="form-check-label text-muted small" for="remember">Ghi nhớ đăng nhập (Cookie)</label>
            </div>

            <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold"><i class="fa-solid fa-arrow-right-to-bracket me-1"></i> Đăng Nhập</button>
        </form>

        <div class="mt-4 pt-3 border-top text-center">
            <div class="bg-light p-2 rounded small text-start mb-2">
                <span class="fw-bold text-secondary">Tài khoản demo:</span><br>
                • <code>admin</code> / <code>123</code> (Admin)<br>
                • <code>trungnh</code> / <code>123</code> (Admin)<br>
                • <code>user1</code> / <code>123456</code> (User)
            </div>
            <a href="${pageContext.request.contextPath}/home" class="text-decoration-none small text-muted"><i class="fa-solid fa-house me-1"></i> Về trang chủ</a>
        </div>
    </div>
</body>
</html>
