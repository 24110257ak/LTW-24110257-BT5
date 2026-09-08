<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng Nhập - Koha Store</title>
</head>
<body>
    <div class="row justify-content-center my-4">
        <div class="col-md-6 col-lg-5">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-4 p-md-5">
                    <div class="text-center mb-4">
                        <div class="avatar-lg bg-primary-subtle text-primary rounded-circle d-inline-flex align-items-center justify-content-center p-3 mb-2">
                            <i class="fa-solid fa-lock fa-2x"></i>
                        </div>
                        <h3 class="fw-bold text-dark">Đăng Nhập</h3>
                        <p class="text-muted small">Chào mừng bạn quay trở lại với Koha Store</p>
                    </div>

                    <!-- Alert thông báo thành công hoặc lỗi -->
                    <c:if test="${not empty sessionScope.success_msg}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-circle-check me-2"></i>${sessionScope.success_msg}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <c:remove var="success_msg" scope="session"/>
                    </c:if>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>${error}
                            <c:if test="${not empty unverifiedUsername}">
                                <div class="mt-2">
                                    <a href="<c:url value='/verify-otp?username=${unverifiedUsername}'/>" class="btn btn-sm btn-outline-danger fw-bold">
                                        &raquo; Kích hoạt tài khoản bằng mã OTP ngay
                                    </a>
                                </div>
                            </c:if>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <!-- Form Đăng nhập có Client-side & Server-side validation -->
                    <form action="<c:url value="/login"/>" method="post" class="needs-validation" novalidate>
                        <!-- Username -->
                        <div class="mb-3">
                            <label for="username" class="form-label fw-semibold">
                                Tên đăng nhập <span class="text-danger">*</span>
                            </label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="fa-solid fa-user text-secondary"></i></span>
                                <input type="text" id="username" name="username" 
                                       class="form-control ${not empty errors.username ? 'is-invalid' : ''}" 
                                       value="${not empty rememberUsername ? rememberUsername : ''}" 
                                       placeholder="Nhập username của bạn" 
                                       required minlength="3" maxlength="50">
                                <c:if test="${not empty errors.username}">
                                    <div class="invalid-feedback d-block">${errors.username}</div>
                                </c:if>
                                <div class="invalid-feedback">Vui lòng nhập tên đăng nhập (tối thiểu 3 ký tự).</div>
                            </div>
                        </div>

                        <!-- Password -->
                        <div class="mb-3">
                            <div class="d-flex justify-content-between align-items-center">
                                <label for="password" class="form-label fw-semibold">
                                    Mật khẩu <span class="text-danger">*</span>
                                </label>
                                <a href="<c:url value='/forgot-password'/>" class="small text-decoration-none">
                                    Quên mật khẩu?
                                </a>
                            </div>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="fa-solid fa-key text-secondary"></i></span>
                                <input type="password" id="password" name="password" 
                                       class="form-control ${not empty errors.password ? 'is-invalid' : ''}" 
                                       placeholder="Nhập mật khẩu" 
                                       required minlength="6">
                                <c:if test="${not empty errors.password}">
                                    <div class="invalid-feedback d-block">${errors.password}</div>
                                </c:if>
                                <div class="invalid-feedback">Vui lòng nhập mật khẩu (tối thiểu 6 ký tự).</div>
                            </div>
                        </div>

                        <!-- Remember Me -->
                        <div class="mb-4 form-check">
                            <input type="checkbox" class="form-check-input" id="remember" name="remember" value="on" ${rememberChecked ? 'checked' : ''}>
                            <label class="form-check-label text-muted small" for="remember">
                                Ghi nhớ tài khoản trên thiết bị này (Cookie 7 ngày)
                            </label>
                        </div>

                        <!-- Submit Button -->
                        <button type="submit" class="btn btn-primary w-100 py-2 fw-bold shadow-sm">
                            <i class="fa-solid fa-right-to-bracket me-2"></i>Đăng Nhập
                        </button>
                    </form>

                    <hr class="my-4">

                    <div class="text-center text-muted small">
                        Chưa có tài khoản? 
                        <a href="<c:url value='/register'/>" class="text-primary fw-bold text-decoration-none ms-1">
                            Đăng ký ngay (Xác thực OTP)
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 Client-Side Validation Script -->
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
