<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chỉnh Sửa Danh Mục - Admin Panel</title>
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
                            <i class="fa-solid fa-pen-to-square text-warning me-2"></i>Chỉnh Sửa Danh Mục #${cate.categoryId}
                        </h4>
                    </div>
                </div>

                <div class="card-body p-4 pt-2">
                    <form action="<c:url value="/admin/category/update"/>" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
                        <input type="hidden" name="categoryid" value="${cate.categoryId}">

                        <!-- Category Name -->
                        <div class="mb-3">
                            <label for="categoryname" class="form-label fw-semibold">
                                Tên danh mục <span class="text-danger">*</span>
                            </label>
                            <input type="text" id="categoryname" name="categoryname" 
                                   class="form-control ${not empty errors.categoryname ? 'is-invalid' : ''}" 
                                   value="${cate.categoryname}" required minlength="2" maxlength="100">
                            <c:if test="${not empty errors.categoryname}">
                                <div class="invalid-feedback d-block">${errors.categoryname}</div>
                            </c:if>
                            <div class="invalid-feedback">Tên danh mục bắt buộc từ 2 đến 100 ký tự.</div>
                        </div>

                        <!-- Current Image Preview -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold d-block">Hình ảnh hiện tại:</label>
                            <c:choose>
                                <c:when test="${not empty cate.images and (cate.images.startsWith('http://') or cate.images.startsWith('https://'))}">
                                    <c:url value="${cate.images}" var="imgUrl"></c:url>
                                </c:when>
                                <c:otherwise>
                                    <c:url value="/image?fname=${cate.images}" var="imgUrl"></c:url>
                                </c:otherwise>
                            </c:choose>
                            <div class="p-2 border rounded bg-light d-inline-block">
                                <img src="${imgUrl}" alt="${cate.categoryname}" width="100" height="80" style="object-fit: contain;" class="rounded border">
                            </div>
                        </div>

                        <!-- Image File Upload (New) -->
                        <div class="mb-3">
                            <label for="images1" class="form-label fw-semibold">
                                <i class="fa-solid fa-upload me-1 text-primary"></i>Thay đổi ảnh từ máy tính (Multipart):
                            </label>
                            <input type="file" id="images1" name="images1" 
                                   class="form-control ${not empty errors.imageFile ? 'is-invalid' : ''}" 
                                   accept="image/*">
                            <c:if test="${not empty errors.imageFile}">
                                <div class="invalid-feedback d-block">${errors.imageFile}</div>
                            </c:if>
                            <div class="form-text">Để trống nếu muốn giữ nguyên ảnh hiện tại.</div>
                        </div>

                        <!-- Image Link Online -->
                        <div class="mb-3">
                            <label for="images" class="form-label fw-semibold">
                                <i class="fa-solid fa-link me-1 text-primary"></i>Hoặc thay đổi liên kết ảnh Online:
                            </label>
                            <input type="text" id="images" name="images" 
                                   class="form-control ${not empty errors.images ? 'is-invalid' : ''}" 
                                   value="${cate.images.startsWith('http') ? cate.images : ''}" 
                                   placeholder="https://example.com/category-icon.png">
                            <c:if test="${not empty errors.images}">
                                <div class="invalid-feedback d-block">${errors.images}</div>
                            </c:if>
                        </div>

                        <!-- Status -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold d-block">Trạng thái hoạt động:</label>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" id="ston" name="status" value="1" 
                                       ${cate.status == 1 ? 'checked' : ''}>
                                <label class="form-check-label text-success fw-semibold" for="ston">
                                    <i class="fa-solid fa-check-circle me-1"></i>Hoạt động
                                </label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" id="stoff" name="status" value="0" 
                                       ${cate.status != 1 ? 'checked' : ''}>
                                <label class="form-check-label text-danger fw-semibold" for="stoff">
                                    <i class="fa-solid fa-lock me-1"></i>Tạm khóa
                                </label>
                            </div>
                        </div>

                        <!-- Buttons -->
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-warning px-4 fw-bold shadow-sm">
                                <i class="fa-solid fa-floppy-disk me-1"></i>Lưu Thay Đổi
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
