<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang Chủ - Koha Web Store</title>
</head>
<body>
    <!-- Banner Chào Mừng -->
    <div class="p-4 p-md-5 mb-4 rounded-3 text-bg-primary shadow-sm">
        <div class="col-md-8 px-0">
            <h1 class="display-5 fw-bold"><i class="fa-solid fa-store me-2"></i>Koha Web Store</h1>
            <p class="lead my-3">
                Hệ thống thương mại điện tử phân tầng MVC - Sử dụng Jakarta Servlet 6.0, JPA 3.0 & Hibernate 6.6, 
                được trang trí hoàn chỉnh bằng SiteMesh Decorator 3 kết hợp Bootstrap 5.
            </p>
            <p class="lead mb-0">
                <a href="<c:url value='/product'/>" class="btn btn-warning text-dark fw-bold px-4 py-2">
                    <i class="fa-solid fa-bag-shopping me-2"></i>Khám phá tất cả sản phẩm
                </a>
                <c:if test="${empty sessionScope.user}">
                    <a href="<c:url value='/register'/>" class="btn btn-outline-light ms-2 px-4 py-2">
                        <i class="fa-solid fa-user-plus me-1"></i>Đăng ký ngay
                    </a>
                </c:if>
            </p>
        </div>
    </div>

    <!-- Danh mục sản phẩm nổi bật -->
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body">
            <h5 class="card-title fw-bold text-secondary mb-3">
                <i class="fa-solid fa-tags me-2 text-primary"></i>Danh mục sản phẩm
            </h5>
            <div class="d-flex flex-wrap gap-2">
                <a href="<c:url value='/product'/>" class="btn btn-outline-primary btn-sm rounded-pill px-3">
                    Tất cả danh mục
                </a>
                <c:forEach items="${listCategory}" var="c">
                    <a href="<c:url value='/product?categoryId=${c.categoryId}'/>" class="btn btn-light btn-sm rounded-pill px-3 border">
                        <i class="fa-solid fa-folder me-1 text-warning"></i>${c.categoryname}
                    </a>
                </c:forEach>
            </div>
        </div>
    </div>

    <!-- 10 Sản phẩm mới nhất -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="fw-bold mb-0 text-dark">
            <i class="fa-solid fa-fire text-danger me-2"></i>10 Sản Phẩm Mới Nhất
        </h4>
        <a href="<c:url value='/product'/>" class="text-decoration-none fw-semibold">
            Xem tất cả (${topProducts.size()}+) &raquo;
        </a>
    </div>

    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-5 g-3 mb-4">
        <c:forEach items="${topProducts}" var="p" varStatus="STT">
            <c:choose>
                <c:when test="${not empty p.images and (p.images.startsWith('http://') or p.images.startsWith('https://'))}">
                    <c:url value="${p.images}" var="imgUrl"></c:url>
                </c:when>
                <c:otherwise>
                    <c:url value="/image?fname=${p.images}" var="imgUrl"></c:url>
                </c:otherwise>
            </c:choose>

            <div class="col">
                <div class="card h-100 border-0 shadow-sm product-card transition">
                    <div class="position-relative bg-white text-center p-3" style="height: 180px;">
                        <img src="${imgUrl}" class="card-img-top h-100" alt="${p.productName}" style="object-fit: contain;">
                        <span class="position-absolute top-0 start-0 badge bg-danger m-2">Mới</span>
                    </div>
                    <div class="card-body d-flex flex-direction-column flex-column justify-content-between">
                        <div>
                            <small class="text-muted text-uppercase fw-semibold" style="font-size: 11px;">
                                ${p.category.categoryname}
                            </small>
                            <h6 class="card-title text-truncate fw-bold mt-1 mb-2" title="${p.productName}">
                                <a href="<c:url value='/product/detail?id=${p.productId}'/>" class="text-dark text-decoration-none">
                                    ${p.productName}
                                </a>
                            </h6>
                        </div>
                        <div>
                            <div class="text-danger fw-bold fs-6 mb-2">
                                <fmt:formatNumber value="${p.price}" pattern="#,###"/> đ
                            </div>
                            <a href="<c:url value='/product/detail?id=${p.productId}'/>" class="btn btn-outline-primary btn-sm w-100">
                                <i class="fa-solid fa-eye me-1"></i>Chi tiết
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Bảng chi tiết 10 sản phẩm -->
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-header bg-white py-3">
            <h5 class="card-title fw-bold mb-0 text-secondary">
                <i class="fa-solid fa-table-list me-2 text-primary"></i>Bảng Tổng Hợp 10 Sản Phẩm Mới Nhất
            </h5>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover table-striped align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="text-center" style="width: 60px;">STT</th>
                            <th class="text-center" style="width: 100px;">Hình ảnh</th>
                            <th>Tên sản phẩm</th>
                            <th>Danh mục</th>
                            <th class="text-end" style="width: 150px;">Đơn giá</th>
                            <th class="text-center" style="width: 120px;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${topProducts}" var="p" varStatus="STT">
                            <c:choose>
                                <c:when test="${not empty p.images and (p.images.startsWith('http://') or p.images.startsWith('https://'))}">
                                    <c:url value="${p.images}" var="imgUrl"></c:url>
                                </c:when>
                                <c:otherwise>
                                    <c:url value="/image?fname=${p.images}" var="imgUrl"></c:url>
                                </c:otherwise>
                            </c:choose>
                            <tr>
                                <td class="text-center fw-bold">${STT.index + 1}</td>
                                <td class="text-center">
                                    <img src="${imgUrl}" alt="${p.productName}" width="60" height="50" style="object-fit: contain;" class="rounded border">
                                </td>
                                <td>
                                    <a href="<c:url value='/product/detail?id=${p.productId}'/>" class="text-decoration-none fw-semibold text-primary">
                                        ${p.productName}
                                    </a>
                                </td>
                                <td><span class="badge bg-light text-dark border">${p.category.categoryname}</span></td>
                                <td class="text-end text-danger fw-bold">
                                    <fmt:formatNumber value="${p.price}" pattern="#,###"/> đ
                                </td>
                                <td class="text-center">
                                    <a href="<c:url value='/product/detail?id=${p.productId}'/>" class="btn btn-sm btn-outline-primary">
                                        <i class="fa-solid fa-circle-info me-1"></i>Xem
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
