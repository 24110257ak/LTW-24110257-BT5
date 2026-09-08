<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chỉnh Sửa Người Dùng - Admin Panel</title>
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
                            <i class="fa-solid fa-user-pen text-warning me-2"></i>Chỉnh Sửa Người Dùng #${user.id}
                        </h4>
                    </div>
                </div>

                <div class="card-body p-4 pt-2">
                    <form action="<c:url value='/admin/user/update'/>" method="post" enctype="multipart/form-data" class="needs-validation">
                        <input type="hidden" name="id" value="${user.id}">

                        <!-- Row 1: Username (readonly) & New Password (optional) -->
                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label for="username" class="form-label fw-semibold text-muted">
                                    Tên đăng nhập (Cố định):
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-user-tag"></i></span>
                                    <input type="text" id="username" class="form-control bg-light" 
                                           value="${user.username}" readonly>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label for="password" class="form-label fw-semibold">
                                    Mật khẩu mới:
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-key"></i></span>
                                    <input type="password" id="password" name="password" 
                                           class="form-control ${not empty errors.password ? 'is-invalid' : ''}" 
                                           placeholder="Để trống nếu không đổi...">
                                </div>
                                <c:if test="${not empty errors.password}">
                                    <div class="invalid-feedback d-block">${errors.password}</div>
                                </c:if>
                                <div class="form-text small">Chỉ nhập nếu muốn đổi mật khẩu mới.</div>
                            </div>
                        </div>

                        <!-- Fullname -->
                        <div class="mb-3">
                            <label for="fullname" class="form-label fw-semibold">
                                Họ và tên đầy đủ <span class="text-danger">*</span>
                            </label>
                            <input type="text" id="fullname" name="fullname" 
                                   class="form-control ${not empty errors.fullname ? 'is-invalid' : ''}" 
                                   value="${user.fullName}" required minlength="2" maxlength="100">
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
                                           value="${user.email}" placeholder="vana@gmail.com">
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
                                           value="${user.phone}" placeholder="0912345678">
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
                                    <option value="2" ${user.roleid == 2 ? 'selected' : ''}>User (Khách hàng / Thành viên)</option>
                                    <option value="1" ${user.roleid == 1 ? 'selected' : ''}>Admin (Quản trị viên)</option>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold d-block">Trạng thái tài khoản:</label>
                                <div class="form-check form-check-inline mt-1">
                                    <input class="form-check-input" type="radio" id="st_active" name="status" value="1" 
                                           ${user.status == 1 ? 'checked' : ''}>
                                    <label class="form-check-label text-success fw-semibold" for="st_active">
                                        <i class="fa-solid fa-circle-check me-1"></i>Đã kích hoạt
                                    </label>
                                </div>
                                <div class="form-check form-check-inline mt-1">
                                    <input class="form-check-input" type="radio" id="st_inactive" name="status" value="0" 
                                           ${user.status == 0 ? 'checked' : ''}>
                                    <label class="form-check-label text-secondary fw-semibold" for="st_inactive">
                                        <i class="fa-solid fa-circle-pause me-1"></i>Chưa kích hoạt
                                    </label>
                                </div>
                            </div>
                        </div>

                        <!-- Current Avatar Display -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold d-block">Ảnh đại diện hiện tại:</label>
                            <c:choose>
                                <c:when test="${not empty user.images and (user.images.startsWith('http://') or user.images.startsWith('https://'))}">
                                    <c:url value="${user.images}" var="currentAvatar" />
                                </c:when>
                                <c:when test="${not empty user.images}">
                                    <c:url value="/image?fname=${user.images}" var="currentAvatar" />
                                </c:when>
                                <c:otherwise>
                                    <c:url value="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" var="currentAvatar" />
                                </c:otherwise>
                            </c:choose>
                            <div class="d-flex align-items-center gap-3 p-2 bg-light rounded border">
                                <img id="avatarPreview" src="${currentAvatar}" alt="${user.username}" 
                                     width="70" height="70" class="rounded-circle border shadow-sm object-fit-cover">
                                <div class="text-muted small">
                                    Tải ảnh mới bên dưới để thay đổi avatar hiện tại.
                                </div>
                            </div>
                        </div>

                        <!-- Avatar Upload File (New) -->
                        <div class="mb-3">
                            <label for="avatarFile" class="form-label fw-semibold">
                                <i class="fa-solid fa-upload me-1 text-primary"></i>Thay đổi ảnh từ máy tính:
                            </label>
                            <input type="file" id="avatarFile" name="avatarFile" 
                                   class="form-control ${not empty errors.avatarFile ? 'is-invalid' : ''}" 
                                   accept="image/*" onchange="previewAvatar(this)">
                            <c:if test="${not empty errors.avatarFile}">
                                <div class="invalid-feedback d-block">${errors.avatarFile}</div>
                            </c:if>
                            <div class="form-text">Định dạng: .jpg, .jpeg, .png, .webp (Tối đa 10MB).</div>
                        </div>

                        <!-- Avatar URL Online (New) -->
                        <div class="mb-4">
                            <label for="images" class="form-label fw-semibold">
                                <i class="fa-solid fa-link me-1 text-primary"></i>Hoặc dán URL ảnh Online mới:
                            </label>
                            <input type="text" id="images" name="images" 
                                   class="form-control ${not empty errors.images ? 'is-invalid' : ''}" 
                                   placeholder="https://example.com/avatar.jpg"
                                   oninput="previewUrl(this.value)">
                            <c:if test="${not empty errors.images}">
                                <div class="invalid-feedback d-block">${errors.images}</div>
                            </c:if>
                        </div>

                        <!-- Submit Buttons -->
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-warning px-4 fw-bold shadow-sm">
                                <i class="fa-solid fa-floppy-disk me-1"></i>Lưu Thay Đổi
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

