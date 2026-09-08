<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm Người Dùng Mới - Admin Panel</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-md-9 col-lg-7">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-header bg-white py-3 border-0">
                    <div class="d-flex align-items-center">
                        <a href="<c:url value='/admin/users'/>" class="btn btn-outline-secondary btn-sm me-3">
                            <i class="fa-solid fa-arrow-left"></i>
                        </a>
                        <h4 class="card-title fw-bold text-dark mb-0">
                            <i class="fa-solid fa-user-plus text-success me-2"></i>Thêm Người Dùng Mới
                        </h4>
                    </div>
                </div>

                <div class="card-body p-4 pt-2">
                    <form action="<c:url value='/admin/user/save'/>" method="post" enctype="multipart/form-data" class="needs-validation">
                        
                        <!-- Row 1: Username & Password -->
                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label for="username" class="form-label fw-semibold">
                                    Tên đăng nhập <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-user"></i></span>
                                    <input type="text" id="username" name="username" 
                                           class="form-control ${not empty errors.username ? 'is-invalid' : ''}" 
                                           value="${not empty username ? username : ''}" 
                                           placeholder="vd: nguyenvana" required minlength="3" maxlength="30">
                                </div>
                                <c:if test="${not empty errors.username}">
                                    <div class="invalid-feedback d-block">${errors.username}</div>
                                </c:if>
                            </div>

                            <div class="col-md-6">
                                <label for="password" class="form-label fw-semibold">
                                    Mật khẩu <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-lock"></i></span>
                                    <input type="password" id="password" name="password" 
                                           class="form-control ${not empty errors.password ? 'is-invalid' : ''}" 
                                           placeholder="Nhập mật khẩu..." required minlength="3">
                                </div>
                                <c:if test="${not empty errors.password}">
                                    <div class="invalid-feedback d-block">${errors.password}</div>
                                </c:if>
                            </div>
                        </div>

                        <!-- Fullname -->
                        <div class="mb-3">
                            <label for="fullname" class="form-label fw-semibold">
                                Họ và tên đầy đủ <span class="text-danger">*</span>
                            </label>
                            <input type="text" id="fullname" name="fullname" 
                                   class="form-control ${not empty errors.fullname ? 'is-invalid' : ''}" 
                                   value="${not empty fullname ? fullname : ''}" 
                                   placeholder="vd: Nguyễn Văn A" required minlength="2" maxlength="100">
                            <c:if test="${not empty errors.fullname}">
                                <div class="invalid-feedback d-block">${errors.fullname}</div>
                            </c:if>
                        </div>

                        <!-- Row 2: Email & Phone -->
                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label for="email" class="form-label fw-semibold">Địa chỉ Email:</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-envelope"></i></span>
                                    <input type="email" id="email" name="email" 
                                           class="form-control ${not empty errors.email ? 'is-invalid' : ''}" 
                                           value="${not empty email ? email : ''}" 
                                           placeholder="vana@gmail.com">
                                </div>
                                <c:if test="${not empty errors.email}">
                                    <div class="invalid-feedback d-block">${errors.email}</div>
                                </c:if>
                            </div>

                            <div class="col-md-6">
                                <label for="phone" class="form-label fw-semibold">Số điện thoại:</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-phone"></i></span>
                                    <input type="tel" id="phone" name="phone" 
                                           class="form-control ${not empty errors.phone ? 'is-invalid' : ''}" 
                                           value="${not empty phone ? phone : ''}" 
                                           placeholder="0912345678">
                                </div>
                                <c:if test="${not empty errors.phone}">
                                    <div class="invalid-feedback d-block">${errors.phone}</div>
                                </c:if>
                            </div>
                        </div>

                        <!-- Row 3: Role & Status -->
                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label for="roleid" class="form-label fw-semibold">Vai trò hệ thống:</label>
                                <select id="roleid" name="roleid" class="form-select">
                                    <option value="2" ${roleid == 2 ? 'selected' : ''}>User (Khách hàng / Thành viên)</option>
                                    <option value="1" ${roleid == 1 ? 'selected' : ''}>Admin (Quản trị viên)</option>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold d-block">Trạng thái tài khoản:</label>
                                <div class="form-check form-check-inline mt-1">
                                    <input class="form-check-input" type="radio" id="st_active" name="status" value="1" 
                                           ${empty status || status == 1 ? 'checked' : ''}>
                                    <label class="form-check-label text-success fw-semibold" for="st_active">
                                        <i class="fa-solid fa-circle-check me-1"></i>Đã kích hoạt
                                    </label>
                                </div>
                                <div class="form-check form-check-inline mt-1">
                                    <input class="form-check-input" type="radio" id="st_inactive" name="status" value="0" 
                                           ${status == 0 ? 'checked' : ''}>
                                    <label class="form-check-label text-secondary fw-semibold" for="st_inactive">
                                        <i class="fa-solid fa-circle-pause me-1"></i>Chưa kích hoạt
                                    </label>
                                </div>
                            </div>
                        </div>

                        <!-- Avatar Upload File -->
                        <div class="mb-3">
                            <label for="avatarFile" class="form-label fw-semibold">
                                <i class="fa-solid fa-upload me-1 text-primary"></i>Tải lên ảnh đại diện:
                            </label>
                            <input type="file" id="avatarFile" name="avatarFile" 
                                   class="form-control ${not empty errors.avatarFile ? 'is-invalid' : ''}" 
                                   accept="image/*" onchange="previewAvatar(this)">
                            <c:if test="${not empty errors.avatarFile}">
                                <div class="invalid-feedback d-block">${errors.avatarFile}</div>
                            </c:if>
                            <div class="form-text">Định dạng: .jpg, .jpeg, .png, .webp (Tối đa 10MB).</div>
                        </div>

                        <!-- Avatar URL Online -->
                        <div class="mb-3">
                            <label for="images" class="form-label fw-semibold">
                                <i class="fa-solid fa-link me-1 text-primary"></i>Hoặc liên kết ảnh Avatar Online (URL):
                            </label>
                            <input type="text" id="images" name="images" 
                                   class="form-control ${not empty errors.images ? 'is-invalid' : ''}" 
                                   value="${not empty images ? images : ''}" 
                                   placeholder="https://example.com/avatar.jpg"
                                   oninput="previewUrl(this.value)">
                            <c:if test="${not empty errors.images}">
                                <div class="invalid-feedback d-block">${errors.images}</div>
                            </c:if>
                        </div>

                        <!-- Avatar Preview Box -->
                        <div class="mb-4 d-flex align-items-center gap-3">
                            <img id="avatarPreview" src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" 
                                 alt="Avatar Preview" width="70" height="70" 
                                 class="rounded-circle border shadow-sm object-fit-cover">
                            <div class="text-muted small">Xem trước ảnh đại diện người dùng</div>
                        </div>

                        <!-- Submit Buttons -->
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-success px-4 fw-bold shadow-sm">
                                <i class="fa-solid fa-user-plus me-1"></i>Tạo Tài Khoản
                            </button>
                            <a href="<c:url value='/admin/users'/>" class="btn btn-outline-secondary px-4">
                                Hủy bỏ
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        function previewAvatar(input) {
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('avatarPreview').src = e.target.result;
                };
                reader.readAsDataURL(input.files[0]);
            }
        }
        function previewUrl(url) {
            if (url && (url.startsWith('http://') || url.startsWith('https://'))) {
                document.getElementById('avatarPreview').src = url;
            }
        }
    </script>
</body>
</html>

