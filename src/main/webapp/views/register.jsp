<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng Ký Tài Khoản - Koha Store</title>
</head>
<body>
    <div class="row justify-content-center my-4">
        <div class="col-md-8 col-lg-6">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-4 p-md-5">
                    <div class="text-center mb-4">
                        <div class="avatar-lg bg-success-subtle text-success rounded-circle d-inline-flex align-items-center justify-content-center p-3 mb-2">
                            <i class="fa-solid fa-user-plus fa-2x"></i>
                        </div>
                        <h3 class="fw-bold text-dark">Tạo Tài Khoản Mới</h3>
                        <p class="text-muted small">Đăng ký thành viên và kích hoạt tài khoản bằng mã xác thực OTP</p>
                    </div>

                    <!-- Alert lỗi chung nếu có -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="<c:url value="/register"/>" method="post" class="needs-validation" novalidate id="registerForm">
                        <div class="row g-3">
                            <!-- Username -->
                            <div class="col-md-6">
                                <label for="username" class="form-label fw-semibold">
                                    Tên đăng nhập <span class="text-danger">*</span>
                                </label>
                                <div class="input-group has-validation">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-at text-secondary"></i></span>
                                    <input type="text" id="username" name="username" 
                                           class="form-control ${not empty errors.username ? 'is-invalid' : ''}" 
                                           value="${not empty oldUsername ? oldUsername : ''}" 
                                           placeholder="vd: nguyenvana" required minlength="3" maxlength="50" pattern="^[a-zA-Z0-9_]{3,50}$">
                                    <c:if test="${not empty errors.username}">
                                        <div class="invalid-feedback d-block">${errors.username}</div>
                                    </c:if>
                                    <div class="invalid-feedback">Tên đăng nhập từ 3-50 ký tự (chữ cái, số, _).</div>
                                </div>
                            </div>

                            <!-- Full Name -->
                            <div class="col-md-6">
                                <label for="fullname" class="form-label fw-semibold">
                                    Họ và tên <span class="text-danger">*</span>
                                </label>
                                <div class="input-group has-validation">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-id-card text-secondary"></i></span>
                                    <input type="text" id="fullname" name="fullname" 
                                           class="form-control ${not empty errors.fullname ? 'is-invalid' : ''}" 
                                           value="${not empty oldFullname ? oldFullname : ''}" 
                                           placeholder="vd: Nguyễn Văn A" required minlength="2" maxlength="100">
                                    <c:if test="${not empty errors.fullname}">
                                        <div class="invalid-feedback d-block">${errors.fullname}</div>
                                    </c:if>
                                    <div class="invalid-feedback">Họ tên từ 2 đến 100 ký tự.</div>
                                </div>
                            </div>

                            <!-- Email -->
                            <div class="col-md-6">
                                <label for="email" class="form-label fw-semibold">
                                    Địa chỉ Email <span class="text-danger">*</span>
                                </label>
                                <div class="input-group has-validation">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-envelope text-secondary"></i></span>
                                    <input type="email" id="email" name="email" 
                                           class="form-control ${not empty errors.email ? 'is-invalid' : ''}" 
                                           value="${not empty oldEmail ? oldEmail : ''}" 
                                           placeholder="name@example.com" required>
                                    <c:if test="${not empty errors.email}">
                                        <div class="invalid-feedback d-block">${errors.email}</div>
                                    </c:if>
                                    <div class="invalid-feedback">Vui lòng nhập địa chỉ email hợp lệ.</div>
                                </div>
                            </div>

                            <!-- Phone -->
                            <div class="col-md-6">
                                <label for="phone" class="form-label fw-semibold">
                                    Số điện thoại
                                </label>
                                <div class="input-group has-validation">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-phone text-secondary"></i></span>
                                    <input type="tel" id="phone" name="phone" 
                                           class="form-control ${not empty errors.phone ? 'is-invalid' : ''}" 
                                           value="${not empty oldPhone ? oldPhone : ''}" 
                                           placeholder="0912345678" pattern="^(0[3|5|7|8|9])[0-9]{8}$">
                                    <c:if test="${not empty errors.phone}">
                                        <div class="invalid-feedback d-block">${errors.phone}</div>
                                    </c:if>
                                    <div class="invalid-feedback">Số điện thoại 10 số (bắt đầu 03, 05, 07, 08, 09).</div>
                                </div>
                            </div>

                            <!-- Password -->
                            <div class="col-md-6">
                                <label for="password" class="form-label fw-semibold">
                                    Mật khẩu <span class="text-danger">*</span>
                                </label>
                                <div class="input-group has-validation">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-lock text-secondary"></i></span>
                                    <input type="password" id="password" name="password" 
                                           class="form-control ${not empty errors.password ? 'is-invalid' : ''}" 
                                           placeholder="Tối thiểu 6 ký tự" required minlength="6">
                                    <c:if test="${not empty errors.password}">
                                        <div class="invalid-feedback d-block">${errors.password}</div>
                                    </c:if>
                                    <div class="invalid-feedback">Mật khẩu phải từ 6 ký tự trở lên.</div>
                                </div>
                            </div>

                            <!-- Confirm Password -->
                            <div class="col-md-6">
                                <label for="confirmPassword" class="form-label fw-semibold">
                                    Xác nhận mật khẩu <span class="text-danger">*</span>
                                </label>
                                <div class="input-group has-validation">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-shield-check text-secondary"></i></span>
                                    <input type="password" id="confirmPassword" name="confirmPassword" 
                                           class="form-control ${not empty errors.confirmPassword ? 'is-invalid' : ''}" 
                                           placeholder="Nhập lại mật khẩu" required minlength="6">
                                    <c:if test="${not empty errors.confirmPassword}">
                                        <div class="invalid-feedback d-block">${errors.confirmPassword}</div>
                                    </c:if>
                                    <div class="invalid-feedback" id="confirmErrorMsg">Vui lòng xác nhận lại mật khẩu.</div>
                                </div>
                            </div>
                        </div>

                        <div class="mt-4">
                            <button type="submit" class="btn btn-success w-100 py-2 fw-bold shadow-sm">
                                <i class="fa-solid fa-paper-plane me-2"></i>Đăng Ký & Nhận Mã OTP
                            </button>
                        </div>
                    </form>

                    <hr class="my-4">

                    <div class="text-center text-muted small">
                        Đã có tài khoản? 
                        <a href="<c:url value='/login'/>" class="text-primary fw-bold text-decoration-none ms-1">
                            Đăng nhập ngay
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Client-side Validation JS -->
    <script>
        (() => {
            'use strict';
            const form = document.getElementById('registerForm');
            const pass = document.getElementById('password');
            const confirmPass = document.getElementById('confirmPassword');
            const confirmErrorMsg = document.getElementById('confirmErrorMsg');

            form.addEventListener('submit', event => {
                // Kiểm tra khớp mật khẩu
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
