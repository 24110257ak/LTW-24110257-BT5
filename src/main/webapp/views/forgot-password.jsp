<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quên Mật Khẩu - Koha Store</title>
</head>
<body>
    <div class="row justify-content-center my-4">
        <div class="col-md-6 col-lg-5">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-4 p-md-5 text-center">
                    <div class="avatar-lg bg-warning-subtle text-warning rounded-circle d-inline-flex align-items-center justify-content-center p-3 mb-3">
                        <i class="fa-solid fa-key fa-2x"></i>
                    </div>
                    <h3 class="fw-bold text-dark">Quên Mật Khẩu</h3>
                    <p class="text-muted small">
                        Nhập tên đăng nhập hoặc email bạn đã đăng ký để nhận mã OTP khôi phục mật khẩu.
                    </p>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show text-start" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="<c:url value="/forgot-password"/>" method="post" class="needs-validation my-4" novalidate>
                        <div class="mb-3 text-start">
                            <label for="account" class="form-label fw-semibold">
                                Tên đăng nhập hoặc Email <span class="text-danger">*</span>
                            </label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="fa-solid fa-user text-secondary"></i></span>
                                <input type="text" id="account" name="account" 
                                       class="form-control" 
                                       value="${not empty account ? account : ''}" 
                                       placeholder="vd: nguyenvana hoặc name@example.com" 
                                       required minlength="3">
                                <div class="invalid-feedback">Vui lòng nhập tên đăng nhập hoặc email.</div>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-warning w-100 py-2 fw-bold text-dark shadow-sm">
                            <i class="fa-solid fa-paper-plane me-2"></i>Gửi Mã Xác Thực OTP
                        </button>
                    </form>

                    <div class="d-flex justify-content-between align-items-center mt-3 small">
                        <a href="<c:url value="/login"/>" class="text-primary text-decoration-none">
                            <i class="fa-solid fa-arrow-left me-1"></i>Quay lại Đăng nhập
                        </a>
                        <a href="<c:url value="/register"/>" class="text-muted text-decoration-none">
                            Đăng ký tài khoản mới
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        (() => {
            'use strict';
            const forms = document.querySelectorAll('.needs-validation');
            Array.from(forms).forEach(form => {
                form.addEventListener('submit', event => {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>
</body>
</html>
