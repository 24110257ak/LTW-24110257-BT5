<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi Tiết Sản Phẩm - ${product.productName}</title>
</head>
<body>
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-3">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="<c:url value='/home'/>">Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="<c:url value='/product'/>">Sản phẩm</a></li>
            <li class="breadcrumb-item active" aria-current="page">${product.productName}</li>
        </ol>
    </nav>

    <c:choose>
        <c:when test="${not empty product.images and (product.images.startsWith('http://') or product.images.startsWith('https://'))}">
            <c:url value="${product.images}" var="imgUrl"></c:url>
        </c:when>
        <c:otherwise>
            <c:url value="/image?fname=${product.images}" var="imgUrl"></c:url>
        </c:otherwise>
    </c:choose>

    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body p-4 p-md-5">
            <div class="row g-5">
                <!-- Cột hình ảnh sản phẩm lớn -->
                <div class="col-md-5 text-center">
                    <div class="p-3 bg-light rounded border d-flex align-items-center justify-content-center" style="min-height: 380px;">
                        <img src="${imgUrl}" alt="${product.productName}" class="img-fluid rounded" style="max-height: 350px; object-fit: contain;">
                    </div>
                </div>

                <!-- Cột thông tin chi tiết -->
                <div class="col-md-7">
                    <div class="mb-2">
                        <span class="badge bg-primary-subtle text-primary border border-primary-subtle px-3 py-2">
                            <i class="fa-solid fa-folder-open me-1"></i>${product.category.categoryname}
                        </span>
                        <c:choose>
                            <c:when test="${product.status == 1}">
                                <span class="badge bg-success-subtle text-success border border-success-subtle px-3 py-2 ms-1">
                                    <i class="fa-solid fa-check me-1"></i>Đang kinh doanh
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-danger-subtle text-danger border border-danger-subtle px-3 py-2 ms-1">
                                    <i class="fa-solid fa-ban me-1"></i>Tạm ngưng bán
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <h2 class="fw-bold text-dark mb-3">${product.productName}</h2>
                    <p class="text-muted small">Mã sản phẩm: <span class="badge bg-light text-dark border">#${product.productId}</span></p>

                    <div class="p-3 bg-light rounded-3 my-3">
                        <div class="text-muted small mb-1">Giá bán niêm yết:</div>
                        <div class="text-danger fw-bold display-6">
                            <fmt:formatNumber value="${product.price}" pattern="#,###"/> <span class="fs-4">VNĐ</span>
                        </div>
                    </div>

                    <div class="mb-3">
                        <p class="mb-1">
                            <i class="fa-solid fa-cubes-stacked me-2 text-secondary"></i>Số lượng tồn kho: 
                            <strong class="text-dark">${product.quantity} sản phẩm</strong>
                        </p>
                    </div>

                    <div class="mb-4">
                        <h6 class="fw-bold text-secondary mb-2">Mô tả sản phẩm:</h6>
                        <div class="p-3 bg-white border rounded text-muted" style="line-height: 1.7;">
                            <c:choose>
                                <c:when test="${not empty product.description}">
                                    ${product.description}
                                </c:when>
                                <c:otherwise>
                                    <i>Chưa có mô tả chi tiết cho sản phẩm này.</i>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="d-flex gap-2">
                        <a href="<c:url value='/product'/>" class="btn btn-outline-secondary px-4">
                            <i class="fa-solid fa-arrow-left me-1"></i>Về danh sách
                        </a>
                        <c:if test="${not empty sessionScope.user and sessionScope.user.roleid == 1}">
                            <a href="<c:url value='/admin/product/edit?id=${product.productId}'/>" class="btn btn-warning px-4">
                                <i class="fa-solid fa-pen-to-square me-1"></i>Chỉnh sửa (Admin)
                            </a>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
