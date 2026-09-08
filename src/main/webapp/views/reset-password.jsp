<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đặt Lại Mật Khẩu - Koha Store</title>
</head>
<body>
    <div class="row justify-content-center my-4">
        <div class="col-md-6 col-lg-5">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-4 p-md-5 text-center">
                    <div class="avatar-lg bg-danger-subtle text-danger rounded-circle d-inline-flex align-items-center justify-content-center p-3 mb-3">
                        <i class="fa-solid fa-lock-open fa-2x"></i>
                    </div>
                    <h3 class="fw-bold text-dark">Đặt Lại Mật Khẩu</h3>
                    <p class="text-muted small">
                        Nhập mã OTP và thiết lập mật khẩu mới cho tài khoản của bạn.
                    </p>

                    <c:if test="${not empty sessionScope.message}">
                        <div class="alert alert-info alert-dismissible fade show text-start" role="alert">
                            <i class="fa-solid fa-circle-info me-2"></i>${sessionScope.message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <c:remove var="message" scope="session"/>
                    </c:if>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show text-start" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="<c:url value="/reset-password"/>" method="post" class="needs-validation text-start my-4" novalidate id="resetForm">
                        <input type="hidden" name="account" value="${account}">

                        <!-- OTP -->
                        <div class="mb-3">
                            <label for="otp" class="form-label fw-semibold">
                                Mã xác thực OTP (6 chữ số) <span class="text-danger">*</span>
                            </label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="fa-solid fa-shield-halved text-secondary"></i></span>
                                <input type="text" id="otp" name="otp" 
                                       class="form-control fw-bold letter-spacing-2" 
                                       maxlength="6" required autofocus placeholder="123456" 
                                       pattern="^[0-9]{6}$">
                                <div class="invalid-feedback">Mã OTP phải gồm đúng 6 chữ số.</div>
                            </div>
                        </div>

                        <!-- New Password -->
                        <div class="mb-3">
                            <label for="newPassword" class="form-label fw-semibold">
                                Mật khẩu mới <span class="text-danger">*</span>
                            </label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="fa-solid fa-key text-secondary"></i></span>
                                <input type="password" id="newPassword" name="newPassword" 
                                       class="form-control" placeholder="Tối thiểu 6 ký tự" 
                                       required minlength="6">
                                <div class="invalid-feedback">Mật khẩu mới tối thiểu 6 ký tự.</div>
                            </div>
                        </div>

                        <!-- Confirm Password -->
                        <div class="mb-4">
                            <label for="confirmPassword" class="form-label fw-semibold">
                                Xác nhận mật khẩu mới <span class="text-danger">*</span>
                            </label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="fa-solid fa-check-double text-secondary"></i></span>
                                <input type="password" id="confirmPassword" name="confirmPassword" 
                                       class="form-control" placeholder="Nhập lại mật khẩu mới" 
                                       required minlength="6">
                                <div class="invalid-feedback" id="confirmErrorMsg">Vui lòng xác nhận lại mật khẩu mới.</div>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-danger w-100 py-2 fw-bold shadow-sm">
                            <i class="fa-solid fa-rotate me-2"></i>Đổi Mật Khẩu
                        </button>
                    </form>

                    <div class="mt-3 small">
                        <a href="<c:url value="/login"/>" class="text-primary text-decoration-none">
                            <i class="fa-solid fa-arrow-left me-1"></i>Quay lại Đăng nhập
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        (() => {
            'use strict';
            const form = document.getElementById('resetForm');
            const pass = document.getElementById('newPassword');
            const confirmPass = document.getElementById('confirmPassword');
            const confirmErrorMsg = document.getElementById('confirmErrorMsg');

            form.addEventListener('submit', event => {
                if (pass.value !== confirmPass.value) {
                    confirmPass.setCustomValidity('Mật khẩu xác nhận không khớp!');
                    confirmErrorMsg.textContent = 'Mật khẩu xác nhận không khớp!';
                } else {
                    confirmPass.setCustomValidity('');
                }

                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);

            confirmPass.addEventListener('input', () => {
                if (pass.value !== confirmPass.value) {
                    confirmPass.setCustomValidity('Mật khẩu xác nhận không khớp!');
                } else {
                    confirmPass.setCustomValidity('');
                }
            });
        })();
    </script>
</body>
</html>
