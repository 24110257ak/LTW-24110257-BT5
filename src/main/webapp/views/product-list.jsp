<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh Sách Sản Phẩm - Koha Store</title>
</head>
<body>
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-3">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="<c:url value='/home'/>">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Sản phẩm</li>
            <c:if test="${not empty selectedCategoryId}">
                <li class="breadcrumb-item active text-primary fw-semibold">
                    Lọc theo danh mục
                </li>
            </c:if>
        </ol>
    </nav>

    <!-- Header & Search Box -->
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body p-4">
            <div class="row g-3 align-items-center justify-content-between">
                <div class="col-md-5">
                    <h4 class="fw-bold text-dark mb-1">
                        <i class="fa-solid fa-boxes-stacked text-primary me-2"></i>Tất Cả Sản Phẩm
                    </h4>
                    <p class="text-muted small mb-0">
                        Hiển thị <b>${listProduct.size()}</b> / Tổng số <b>${totalProducts}</b> sản phẩm (Trang <b>${currentPage}</b>/<b>${totalPages}</b>)
                    </p>
                </div>
                <div class="col-md-7">
                    <form action="<c:url value="/product"/>" method="get" class="d-flex gap-2 justify-content-md-end">
                        <c:if test="${not empty selectedCategoryId}">
                            <input type="hidden" name="categoryId" value="${selectedCategoryId}">
                        </c:if>
                        <div class="input-group" style="max-width: 360px;">
                            <input type="text" name="keyword" class="form-control" placeholder="Tìm theo tên sản phẩm..." value="${not empty keyword ? keyword : ''}">
                            <button type="submit" class="btn btn-primary">
                                <i class="fa-solid fa-magnifying-glass me-1"></i>Tìm
                            </button>
                        </div>
                        <c:if test="${not empty keyword or not empty selectedCategoryId}">
                            <a href="<c:url value="/product"/>" class="btn btn-outline-secondary" title="Xóa toàn bộ bộ lọc">
                                <i class="fa-solid fa-rotate-left"></i>
                            </a>
                        </c:if>
                    </form>
                </div>
            </div>

            <!-- Bộ lọc danh mục dạng pills -->
            <hr class="my-3">
            <div class="d-flex align-items-center flex-wrap gap-2">
                <span class="text-muted small fw-semibold me-2"><i class="fa-solid fa-filter me-1"></i>Danh mục:</span>
                <a href="<c:url value="/product${not empty keyword ? '?keyword='.concat(keyword) : ''}"/>" 
                   class="btn btn-sm rounded-pill ${empty selectedCategoryId ? 'btn-primary' : 'btn-light border'}">
                    Tất cả
                </a>
                <c:forEach items="${listCategory}" var="c">
                    <a href="<c:url value="/product?categoryId=${c.categoryId}${not empty keyword ? '&keyword='.concat(keyword) : ''}"/>" 
                       class="btn btn-sm rounded-pill ${selectedCategoryId == c.categoryId ? 'btn-primary' : 'btn-light border'}">
                        ${c.categoryname}
                    </a>
                </c:forEach>
            </div>
        </div>
    </div>

    <!-- Lưới sản phẩm (Cards) -->
    <c:choose>
        <c:when test="${not empty listProduct}">
            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-3 g-4 mb-4">
                <c:forEach items="${listProduct}" var="p">
                    <c:choose>
                        <c:when test="${not empty p.images and (p.images.startsWith('http://') or p.images.startsWith('https://'))}">
                            <c:url value="${p.images}" var="imgUrl"></c:url>
                        </c:when>
                        <c:otherwise>
                            <c:url value="/image?fname=${p.images}" var="imgUrl"></c:url>
                        </c:otherwise>
                    </c:choose>

                    <div class="col">
                        <div class="card h-100 border-0 shadow-sm transition">
                            <div class="bg-white text-center p-3 position-relative" style="height: 220px;">
                                <img src="${imgUrl}" class="card-img-top h-100" alt="${p.productName}" style="object-fit: contain;">
                                <span class="position-absolute top-0 end-0 badge bg-secondary m-2">
                                    Kho: ${p.quantity}
                                </span>
                            </div>
                            <div class="card-body d-flex flex-column justify-content-between">
                                <div>
                                    <div class="d-flex justify-content-between align-items-center mb-1">
                                        <span class="badge bg-light text-primary border">${p.category.categoryname}</span>
                                        <c:choose>
                                            <c:when test="${p.status == 1}">
                                                <span class="badge bg-success-subtle text-success border border-success-subtle">Còn hàng</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger-subtle text-danger border border-danger-subtle">Hết hàng</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <h5 class="card-title fw-bold text-truncate mt-2" title="${p.productName}">
                                        <a href="<c:url value='/product/detail?id=${p.productId}'/>" class="text-dark text-decoration-none">
                                            ${p.productName}
                                        </a>
                                    </h5>
                                    <p class="card-text text-muted small" style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; height: 38px;">
                                        ${not empty p.description ? p.description : 'Chưa có mô tả chi tiết cho sản phẩm này.'}
                                    </p>
                                </div>
                                <div class="pt-2 border-top">
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <span class="text-muted small">Đơn giá:</span>
                                        <span class="text-danger fw-bold fs-5">
                                            <fmt:formatNumber value="${p.price}" pattern="#,###"/> đ
                                        </span>
                                    </div>
                                    <a href="<c:url value='/product/detail?id=${p.productId}'/>" class="btn btn-primary btn-sm w-100">
                                        <i class="fa-solid fa-circle-info me-1"></i>Xem chi tiết
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Phân trang Bootstrap 5 (6 sản phẩm / trang) -->
            <c:if test="${totalPages > 1}">
                <nav aria-label="Product pagination" class="my-4">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                            <a class="page-link" href="<c:url value="/product?page=${currentPage - 1}${not empty selectedCategoryId ? '&categoryId='.concat(selectedCategoryId) : ''}${not empty keyword ? '&keyword='.concat(keyword) : ''}"/>">
                                <i class="fa-solid fa-chevron-left"></i>
                            </a>
                        </li>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link" href="<c:url value="/product?page=${i}${not empty selectedCategoryId ? '&categoryId='.concat(selectedCategoryId) : ''}${not empty keyword ? '&keyword='.concat(keyword) : ''}"/>">
                                    ${i}
                                </a>
                            </li>
                        </c:forEach>

                        <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="<c:url value="/product?page=${currentPage + 1}${not empty selectedCategoryId ? '&categoryId='.concat(selectedCategoryId) : ''}${not empty keyword ? '&keyword='.concat(keyword) : ''}"/>">
                                <i class="fa-solid fa-chevron-right"></i>
                            </a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </c:when>
        <c:otherwise>
            <div class="card border-0 shadow-sm text-center py-5">
                <div class="card-body">
                    <i class="fa-solid fa-box-open fa-3x text-muted mb-3"></i>
                    <h5 class="fw-bold text-secondary">Không tìm thấy sản phẩm nào!</h5>
                    <p class="text-muted small">Hãy thử tìm kiếm với từ khóa khác hoặc xóa bộ lọc danh mục.</p>
                    <a href="<c:url value='/product'/>" class="btn btn-outline-primary btn-sm">
                        <i class="fa-solid fa-rotate-left me-1"></i>Xem tất cả sản phẩm
                    </a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>
