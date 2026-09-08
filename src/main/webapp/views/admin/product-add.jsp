<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm Sản Phẩm Mới - Admin Panel</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-md-9 col-lg-8">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-header bg-white py-3 border-0">
                    <div class="d-flex align-items-center">
                        <a href="<c:url value='/admin/products'/>" class="btn btn-outline-secondary btn-sm me-3">
                            <i class="fa-solid fa-arrow-left"></i>
                        </a>
                        <h4 class="card-title fw-bold text-dark mb-0">
                            <i class="fa-solid fa-cart-plus text-success me-2"></i>Thêm Sản Phẩm Mới
                        </h4>
                    </div>
                </div>

                <div class="card-body p-4 pt-2">
                    <form action="<c:url value="/admin/product/insert"/>" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
                        <div class="row g-3">
                            <!-- Product Name -->
                            <div class="col-md-8">
                                <label for="productName" class="form-label fw-semibold">
                                    Tên sản phẩm <span class="text-danger">*</span>
                                </label>
                                <input type="text" id="productName" name="productName" 
                                       class="form-control ${not empty errors.productName ? 'is-invalid' : ''}" 
                                       value="${not empty oldName ? oldName : ''}" 
                                       placeholder="vd: iPhone 16 Pro Max 256GB" 
                                       required minlength="2" maxlength="200">
                                <c:if test="${not empty errors.productName}">
                                    <div class="invalid-feedback d-block">${errors.productName}</div>
                                </c:if>
                                <div class="invalid-feedback">Tên sản phẩm bắt buộc từ 2 đến 200 ký tự.</div>
                            </div>

                            <!-- Category -->
                            <div class="col-md-4">
                                <label for="categoryId" class="form-label fw-semibold">
                                    Danh mục <span class="text-danger">*</span>
                                </label>
                                <select id="categoryId" name="categoryId" 
                                        class="form-select ${not empty errors.categoryId ? 'is-invalid' : ''}" required>
                                    <option value="">-- Chọn danh mục --</option>
                                    <c:forEach items="${categories}" var="c">
                                        <option value="${c.categoryId}" ${oldCategoryId == c.categoryId ? 'selected' : ''}>
                                            ${c.categoryname}
                                        </option>
                                    </c:forEach>
                                </select>
                                <c:if test="${not empty errors.categoryId}">
                                    <div class="invalid-feedback d-block">${errors.categoryId}</div>
                                </c:if>
                                <div class="invalid-feedback">Vui lòng chọn danh mục cho sản phẩm.</div>
                            </div>

                            <!-- Price -->
                            <div class="col-md-6">
                                <label for="price" class="form-label fw-semibold">
                                    Đơn giá (VNĐ) <span class="text-danger">*</span>
                                </label>
                                <div class="input-group has-validation">
                                    <input type="number" id="price" name="price" min="1000" step="1000" 
                                           class="form-control ${not empty errors.price ? 'is-invalid' : ''}" 
                                           value="${not empty oldPrice ? oldPrice : ''}" 
                                           placeholder="vd: 25000000" required>
                                    <span class="input-group-text">đ</span>
                                    <c:if test="${not empty errors.price}">
                                        <div class="invalid-feedback d-block">${errors.price}</div>
                                    </c:if>
                                    <div class="invalid-feedback">Đơn giá phải là số dương lớn hơn 0.</div>
                                </div>
                            </div>

                            <!-- Quantity -->
                            <div class="col-md-6">
                                <label for="quantity" class="form-label fw-semibold">
                                    Số lượng trong kho <span class="text-danger">*</span>
                                </label>
                                <input type="number" id="quantity" name="quantity" min="0" step="1" 
                                       class="form-control ${not empty errors.quantity ? 'is-invalid' : ''}" 
                                       value="${not empty oldQuantity ? oldQuantity : '10'}" 
                                       required>
                                <c:if test="${not empty errors.quantity}">
                                    <div class="invalid-feedback d-block">${errors.quantity}</div>
                                </c:if>
                                <div class="invalid-feedback">Số lượng phải là số nguyên không âm (&gt;= 0).</div>
                            </div>

                            <!-- Image File (Multipart) -->
                            <div class="col-md-6">
                                <label for="imageFile" class="form-label fw-semibold">
                                    <i class="fa-solid fa-upload me-1 text-primary"></i>Tải ảnh từ máy tính:
                                </label>
                                <input type="file" id="imageFile" name="imageFile" 
                                       class="form-control ${not empty errors.imageFile ? 'is-invalid' : ''}" 
                                       accept="image/*">
                                <c:if test="${not empty errors.imageFile}">
                                    <div class="invalid-feedback d-block">${errors.imageFile}</div>
                                </c:if>
                                <div class="form-text">Định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp (Tối đa 10MB).</div>
                            </div>

                            <!-- Image Link -->
                            <div class="col-md-6">
                                <label for="imageLink" class="form-label fw-semibold">
                                    <i class="fa-solid fa-link me-1 text-primary"></i>Hoặc dán liên kết ảnh Online:
                                </label>
                                <input type="text" id="imageLink" name="imageLink" 
                                       class="form-control ${not empty errors.imageLink ? 'is-invalid' : ''}" 
                                       value="${not empty oldImageLink ? oldImageLink : ''}" 
                                       placeholder="https://example.com/image.jpg">
                                <c:if test="${not empty errors.imageLink}">
                                    <div class="invalid-feedback d-block">${errors.imageLink}</div>
                                </c:if>
                                <div class="form-text">Bắt đầu bằng http:// hoặc https://.</div>
                            </div>

                            <!-- Description -->
                            <div class="col-12">
                                <label for="description" class="form-label fw-semibold">Mô tả sản phẩm:</label>
                                <textarea id="description" name="description" rows="4" 
                                          class="form-control" 
                                          placeholder="Nhập thông số kỹ thuật, bảo hành, đặc điểm nổi bật...">${not empty oldDescription ? oldDescription : ''}</textarea>
                            </div>

                            <!-- Status -->
                            <div class="col-12">
                                <label class="form-label fw-semibold d-block">Trạng thái kinh doanh:</label>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" id="ston" name="status" value="1" 
                                           ${empty oldStatus || oldStatus == 1 ? 'checked' : ''}>
                                    <label class="form-check-label text-success fw-semibold" for="ston">
                                        <i class="fa-solid fa-check-circle me-1"></i>Đang kinh doanh
                                    </label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" id="stoff" name="status" value="0" 
                                           ${oldStatus == 0 ? 'checked' : ''}>
                                    <label class="form-check-label text-danger fw-semibold" for="stoff">
                                        <i class="fa-solid fa-ban me-1"></i>Tạm ngừng bán
                                    </label>
                                </div>
                            </div>
                        </div>

                        <!-- Buttons -->
                        <div class="d-flex gap-2 mt-4">
                            <button type="submit" class="btn btn-success px-4 fw-bold shadow-sm">
                                <i class="fa-solid fa-plus me-1"></i>Thêm Sản Phẩm
                            </button>
                            <a href="<c:url value='/admin/products'/>" class="btn btn-outline-secondary px-4">
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
