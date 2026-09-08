<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hồ Sơ Cá Nhân - ${user.fullName}</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-lg-10">
            <!-- Breadcrumb -->
            <nav aria-label="breadcrumb" class="mb-4">
                <ol class="breadcrumb bg-white px-3 py-2 rounded shadow-sm">
                    <li class="breadcrumb-item"><a href="<c:url value='/home'/>"><i class="fa-solid fa-house"></i> Trang chủ</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Hồ sơ cá nhân</li>
                </ol>
            </nav>

            <!-- Alerts -->
            <c:if test="${not empty sessionScope.success_msg}">
                <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
                    <i class="fa-solid fa-circle-check me-2"></i>${sessionScope.success_msg}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="success_msg" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.error_msg}">
                <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
                    <i class="fa-solid fa-circle-exclamation me-2"></i>${sessionScope.error_msg}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="error_msg" scope="session" />
            </c:if>

            <div class="row g-4">
                <!-- Left: Avatar & Identity Card -->
                <div class="col-md-4">
                    <div class="card border-0 shadow-sm text-center p-4 h-100">
                        <div class="card-body d-flex flex-column align-items-center">
                            <c:choose>
                                <c:when test="${not empty user.images and (user.images.startsWith('http://') or user.images.startsWith('https://'))}">
                                    <c:url value="${user.images}" var="avatarSrc" />
                                </c:when>
                                <c:when test="${not empty user.images}">
                                    <c:url value="/image?fname=${user.images}" var="avatarSrc" />
                                </c:when>
                                <c:otherwise>
                                    <c:set var="avatarSrc" value="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" />
                                </c:otherwise>
                            </c:choose>

                            <div class="position-relative mb-3">
                                <img id="avatarPreview" src="${avatarSrc}" alt="Avatar"
                                     class="rounded-circle border border-3 border-primary shadow-sm"
                                     style="width: 150px; height: 150px; object-fit: cover;" />
                            </div>

                            <h5 class="fw-bold mb-1">${user.fullName}</h5>
                            <p class="text-muted mb-2">@${user.username}</p>

                            <div class="mb-3">
                                <c:choose>
                                    <c:when test="${user.roleid == 1}">
                                        <span class="badge bg-warning text-dark"><i class="fa-solid fa-crown me-1"></i>Quản trị viên (Admin)</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-info text-dark"><i class="fa-solid fa-user me-1"></i>Khách hàng (User)</span>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${user.status == 1}">
                                        <span class="badge bg-success"><i class="fa-solid fa-check me-1"></i>Đã kích hoạt</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">Chưa kích hoạt</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <hr class="w-100 my-3">

                            <div class="text-start w-100 small text-muted">
                                <p class="mb-2"><i class="fa-solid fa-envelope me-2 text-primary"></i>${user.email}</p>
                                <p class="mb-0"><i class="fa-solid fa-phone me-2 text-primary"></i>${not empty user.phone ? user.phone : 'Chưa cập nhật'}</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Right: Profile Edit Form -->
                <div class="col-md-8">
                    <div class="card border-0 shadow-sm p-4">
                        <div class="card-header bg-transparent border-0 pb-0">
                            <h4 class="card-title fw-bold text-primary mb-1">
                                <i class="fa-solid fa-user-pen me-2"></i>Cập Nhật Thông Tin Cá Nhân
                            </h4>
                            <p class="text-muted small">Cập nhật họ tên, số điện thoại và ảnh đại diện của bạn qua JPA & Multipart Upload</p>
                        </div>
                        <div class="card-body pt-3">
                            <form action="<c:url value='/profile'/>" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
                                <div class="row g-3">
                                    <!-- Username (Readonly) -->
                                    <div class="col-md-6">
                                        <label class="form-label fw-semibold">Tên đăng nhập (Username):</label>
                                        <input type="text" class="form-control bg-light" value="${user.username}" readonly />
                                        <div class="form-text">Tên đăng nhập không thể thay đổi.</div>
                                    </div>

                                    <!-- Email (Readonly) -->
                                    <div class="col-md-6">
                                        <label class="form-label fw-semibold">Địa chỉ Email:</label>
                                        <input type="email" class="form-control bg-light" value="${user.email}" readonly />
                                        <div class="form-text">Email dùng để xác thực OTP đăng ký & quên MK.</div>
                                    </div>

                                    <!-- Fullname (Editable) -->
                                    <div class="col-md-6">
                                        <label for="fullname" class="form-label fw-semibold">Họ và tên <span class="text-danger">*</span>:</label>
                                        <input type="text" class="form-control ${not empty errors.fullname ? 'is-invalid' : ''}" id="fullname" name="fullname"
                                               value="${user.fullName}" required minlength="2" maxlength="100" placeholder="Nhập họ và tên..." />
                                        <c:if test="${not empty errors.fullname}">
                                            <div class="invalid-feedback d-block">${errors.fullname}</div>
                                        </c:if>
                                        <div class="invalid-feedback">Họ tên bắt buộc từ 2 đến 100 ký tự.</div>
                                    </div>

                                    <!-- Phone (Editable) -->
                                    <div class="col-md-6">
                                        <label for="phone" class="form-label fw-semibold">Số điện thoại:</label>
                                        <input type="tel" class="form-control ${not empty errors.phone ? 'is-invalid' : ''}" id="phone" name="phone"
                                               value="${user.phone}" placeholder="Ví dụ: 0912345678" pattern="^(0[3|5|7|8|9])[0-9]{8}$" />
                                        <c:if test="${not empty errors.phone}">
                                            <div class="invalid-feedback d-block">${errors.phone}</div>
                                        </c:if>
                                        <div class="invalid-feedback">Số điện thoại 10 chữ số hợp lệ (bắt đầu 03, 05, 07, 08, 09).</div>
                                    </div>

                                    <div class="col-12"><hr class="my-2"></div>

                                    <!-- Image File Upload (Multipart) -->
                                    <div class="col-12">
                                        <label for="imageFile" class="form-label fw-semibold">
                                            <i class="fa-solid fa-upload me-1 text-primary"></i>Tải lên ảnh đại diện từ máy tính (Multipart):
                                        </label>
                                        <input type="file" class="form-control ${not empty errors.imageFile ? 'is-invalid' : ''}" id="imageFile" name="imageFile"
                                               accept="image/*" onchange="previewSelectedImage(this);" />
                                        <c:if test="${not empty errors.imageFile}">
                                            <div class="invalid-feedback d-block">${errors.imageFile}</div>
                                        </c:if>
                                        <div class="form-text">Định dạng chấp nhận: .jpg, .jpeg, .png, .gif, .webp (Tối đa 10MB).</div>
                                    </div>

                                    <!-- Image URL Option -->
                                    <div class="col-12">
                                        <label for="imageLink" class="form-label fw-semibold">
                                            <i class="fa-solid fa-link me-1 text-primary"></i>Hoặc dán đường dẫn ảnh Online (URL):
                                        </label>
                                        <input type="text" class="form-control ${not empty errors.imageLink ? 'is-invalid' : ''}" id="imageLink" name="imageLink"
                                               placeholder="https://example.com/avatar.jpg"
                                               value="${user.images.startsWith('http') ? user.images : ''}"
                                               oninput="previewImageUrl(this.value);" />
                                        <c:if test="${not empty errors.imageLink}">
                                            <div class="invalid-feedback d-block">${errors.imageLink}</div>
                                        </c:if>
                                        <div class="form-text">Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://.</div>
                                    </div>

                                    <!-- Submit & Cancel Buttons -->
                                    <div class="col-12 mt-4 d-flex gap-2">
                                        <button type="submit" class="btn btn-primary px-4 py-2 fw-semibold">
                                            <i class="fa-solid fa-floppy-disk me-2"></i>Lưu Thay Đổi
                                        </button>
                                        <a href="<c:url value='/home'/>" class="btn btn-outline-secondary px-4 py-2">
                                            <i class="fa-solid fa-arrow-left me-1"></i>Quay lại Trang chủ
                                        </a>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript preview avatar dynamically -->
    <script>
        function previewSelectedImage(input) {
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    document.getElementById('avatarPreview').src = e.target.result;
                };
                reader.readAsDataURL(input.files[0]);
            }
        }

        function previewImageUrl(url) {
            if (url && (url.startsWith('http://') || url.startsWith('https://'))) {
                document.getElementById('avatarPreview').src = url;
            }
        }
    </script>
</body>
</html>
