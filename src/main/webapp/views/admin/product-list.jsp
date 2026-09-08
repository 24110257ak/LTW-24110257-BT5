<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Sản Phẩm - Admin Panel</title>
</head>
<body>
    <!-- Title & Add Button -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h3 class="fw-bold text-dark mb-1">
                <i class="fa-solid fa-boxes-stacked text-primary me-2"></i>Quản Lý Sản Phẩm
            </h3>
            <p class="text-muted small mb-0">Hệ thống phân tầng JPA 3.0 & Hibernate 6.6</p>
        </div>
        <div>
            <a href="<c:url value='/admin/product/add'/>" class="btn btn-success shadow-sm">
                <i class="fa-solid fa-plus me-1"></i>Thêm Sản Phẩm Mới
            </a>
        </div>
    </div>

    <!-- Thông báo kết quả -->
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="fa-solid fa-circle-check me-2"></i>${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="message" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
            <i class="fa-solid fa-triangle-exclamation me-2"></i>${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <!-- Search bar -->
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body p-3">
            <form action="<c:url value="/admin/products"/>" method="get" class="row g-2 align-items-center">
                <div class="col-md-5">
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="fa-solid fa-magnifying-glass"></i></span>
                        <input type="text" name="keyword" class="form-control" value="${not empty keyword ? keyword : ''}" placeholder="Nhập tên sản phẩm cần tìm...">
                    </div>
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                    <c:if test="${not empty keyword}">
                        <a href="<c:url value="/admin/products"/>" class="btn btn-outline-secondary">Xóa lọc</a>
                    </c:if>
                </div>
            </form>
        </div>
    </div>

    <!-- Table -->
    <div class="card border-0 shadow-sm">
        <div class="card-header bg-white py-3">
            <h6 class="fw-bold mb-0 text-secondary">
                <i class="fa-solid fa-list me-2 text-primary"></i>Danh Sách Sản Phẩm (${productList.size()})
            </h6>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover table-striped align-middle mb-0">
                    <thead class="table-light text-center">
                        <tr>
                            <th style="width: 50px;">STT</th>
                            <th style="width: 90px;">Hình ảnh</th>
                            <th class="text-start">Tên sản phẩm</th>
                            <th>Danh mục</th>
                            <th class="text-end" style="width: 140px;">Đơn giá</th>
                            <th style="width: 80px;">Số lượng</th>
                            <th style="width: 120px;">Trạng thái</th>
                            <th style="width: 150px;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${productList}" var="p" varStatus="STT">
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
                                    <span class="fw-bold text-dark">${p.productName}</span>
                                    <div class="text-muted small">ID: #${p.productId}</div>
                                </td>
                                <td class="text-center">
                                    <span class="badge bg-light text-primary border">${p.category.categoryname}</span>
                                </td>
                                <td class="text-end text-danger fw-bold">
                                    <fmt:formatNumber value="${p.price}" pattern="#,###"/> đ
                                </td>
                                <td class="text-center">
                                    <span class="badge bg-secondary">${p.quantity}</span>
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${p.status == 1}">
                                            <span class="badge bg-success-subtle text-success border border-success-subtle">
                                                <i class="fa-solid fa-check me-1"></i>Đang bán
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger-subtle text-danger border border-danger-subtle">
                                                <i class="fa-solid fa-ban me-1"></i>Tạm khóa
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <div class="btn-group btn-group-sm">
                                        <a href="<c:url value='/admin/product/edit?id=${p.productId}'/>" class="btn btn-outline-warning" title="Chỉnh sửa">
                                            <i class="fa-solid fa-pen-to-square"></i> Sửa
                                        </a>
                                        <a href="<c:url value='/admin/product/delete?id=${p.productId}'/>" class="btn btn-outline-danger" 
                                           onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm \'${p.productName}\'?')" title="Xóa">
                                            <i class="fa-solid fa-trash"></i> Xóa
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty productList}">
                            <tr>
                                <td colspan="8" class="text-center py-4 text-muted">
                                    <i class="fa-solid fa-circle-exclamation me-1"></i>Không có sản phẩm nào trong hệ thống.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
