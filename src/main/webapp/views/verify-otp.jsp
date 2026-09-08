<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Xác Thực OTP - Koha Store</title>
</head>
<body>
    <div class="row justify-content-center my-4">
        <div class="col-md-6 col-lg-5">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-4 p-md-5 text-center">
                    <div class="avatar-lg bg-info-subtle text-info rounded-circle d-inline-flex align-items-center justify-content-center p-3 mb-3">
                        <i class="fa-solid fa-shield-halved fa-2x"></i>
                    </div>
                    <h3 class="fw-bold text-dark">Xác Thực Mã OTP</h3>
                    <p class="text-muted small">
                        Vui lòng nhập mã OTP 6 chữ số đã được gửi qua email của bạn (hoặc kiểm tra tại Console server).
                    </p>

                    <c:if test="${not empty sessionScope.message}">
                        <div class="alert alert-info alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-envelope me-2"></i>${sessionScope.message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <c:remove var="message" scope="session"/>
                    </c:if>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="<c:url value="/verify-otp"/>" method="post" class="needs-validation my-4" novalidate>
                        <input type="hidden" name="username" value="${username}">
                        
                        <div class="mb-3 text-start">
                            <label for="otp" class="form-label fw-semibold">
                                Mã OTP (6 chữ số) <span class="text-danger">*</span>
                            </label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="fa-solid fa-key text-secondary"></i></span>
                                <input type="text" id="otp" name="otp" 
                                       class="form-control form-control-lg text-center fw-bold letter-spacing-2" 
                                       maxlength="6" required autofocus placeholder="123456" 
                                       pattern="^[0-9]{6}$" style="letter-spacing: 4px; font-size: 22px;">
                                <div class="invalid-feedback">Mã OTP phải bao gồm đúng 6 chữ số.</div>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 fw-bold shadow-sm">
                            <i class="fa-solid fa-check-circle me-2"></i>Kích Hoạt Tài Khoản
                        </button>
                    </form>

                    <div class="d-flex justify-content-between align-items-center mt-3 small">
                        <a href="<c:url value="/resend-otp?username=${username}"/>" class="text-decoration-none">
                            <i class="fa-solid fa-rotate-right me-1"></i>Gửi lại mã OTP
                        </a>
                        <a href="<c:url value="/login"/>" class="text-muted text-decoration-none">
                            Về Đăng nhập
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
