<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm Danh Mục Mới - Admin Panel</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-header bg-white py-3 border-0">
                    <div class="d-flex align-items-center">
                        <a href="<c:url value='/admin/categories'/>" class="btn btn-outline-secondary btn-sm me-3">
                            <i class="fa-solid fa-arrow-left"></i>
                        </a>
                        <h4 class="card-title fw-bold text-dark mb-0">
                            <i class="fa-solid fa-folder-plus text-success me-2"></i>Thêm Danh Mục Mới
                        </h4>
                    </div>
                </div>

                <div class="card-body p-4 pt-2">
                    <form action="<c:url value="/admin/category/insert"/>" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
                        <!-- Category Name -->
                        <div class="mb-3">
                            <label for="categoryname" class="form-label fw-semibold">
                                Tên danh mục <span class="text-danger">*</span>
                            </label>
                            <input type="text" id="categoryname" name="categoryname" 
                                   class="form-control ${not empty errors.categoryname ? 'is-invalid' : ''}" 
                                   value="${not empty oldCategoryName ? oldCategoryName : ''}" 
                                   placeholder="vd: Điện thoại, Laptop, Thời trang..." 
                                   required minlength="2" maxlength="100">
                            <c:if test="${not empty errors.categoryname}">
                                <div class="invalid-feedback d-block">${errors.categoryname}</div>
                            </c:if>
                            <div class="invalid-feedback">Tên danh mục bắt buộc từ 2 đến 100 ký tự.</div>
                        </div>

                        <!-- Image File Upload -->
                        <div class="mb-3">
                            <label for="images1" class="form-label fw-semibold">
                                <i class="fa-solid fa-upload me-1 text-primary"></i>Tải ảnh từ máy tính (Multipart):
                            </label>
                            <input type="file" id="images1" name="images1" 
                                   class="form-control ${not empty errors.imageFile ? 'is-invalid' : ''}" 
                                   accept="image/*">
                            <c:if test="${not empty errors.imageFile}">
                                <div class="invalid-feedback d-block">${errors.imageFile}</div>
                            </c:if>
                            <div class="form-text">Định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp (Tối đa 10MB).</div>
                        </div>

                        <!-- Image Link Online -->
                        <div class="mb-3">
                            <label for="images" class="form-label fw-semibold">
                                <i class="fa-solid fa-link me-1 text-primary"></i>Hoặc dán liên kết ảnh Online (URL):
                            </label>
                            <input type="text" id="images" name="images" 
                                   class="form-control ${not empty errors.images ? 'is-invalid' : ''}" 
                                   value="${not empty oldImages ? oldImages : ''}" 
                                   placeholder="https://example.com/category-icon.png">
                            <c:if test="${not empty errors.images}">
                                <div class="invalid-feedback d-block">${errors.images}</div>
                            </c:if>
                            <div class="form-text">Bắt đầu bằng http:// hoặc https://.</div>
                        </div>

                        <!-- Status -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold d-block">Trạng thái hoạt động:</label>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" id="ston" name="status" value="1" 
                                       ${empty oldStatus || oldStatus == 1 ? 'checked' : ''}>
                                <label class="form-check-label text-success fw-semibold" for="ston">
                                    <i class="fa-solid fa-check-circle me-1"></i>Hoạt động
                                </label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" id="stoff" name="status" value="0" 
                                       ${oldStatus == 0 ? 'checked' : ''}>
                                <label class="form-check-label text-danger fw-semibold" for="stoff">
                                    <i class="fa-solid fa-lock me-1"></i>Tạm khóa
                                </label>
                            </div>
                        </div>

                        <!-- Buttons -->
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-success px-4 fw-bold shadow-sm">
                                <i class="fa-solid fa-plus me-1"></i>Thêm Danh Mục
                            </button>
                            <a href="<c:url value='/admin/categories'/>" class="btn btn-outline-secondary px-4">
                                Hủy bỏ
                            </a>
                        </div>
                    </form>
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
