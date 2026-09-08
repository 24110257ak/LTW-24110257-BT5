<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý danh mục - JPA Dashboard</title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { background-color: #f4f6f9; }
        .card { border-radius: 10px; border: none; box-shadow: 0 4px 6px rgba(0,0,0,0.05); }
        .table img { object-fit: contain; border-radius: 6px; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/admin/categories"><i class="fa-solid fa-layer-group me-2"></i>JPA Category Manager</a>
            <div class="d-flex align-items-center text-white">
                <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-light btn-sm me-2"><i class="fa-solid fa-house"></i> Trang chủ</a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="card p-4 mb-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="text-primary fw-bold mb-0"><i class="fa-solid fa-list me-2"></i>Danh Sách Danh Mục</h4>
                <a href="${pageContext.request.contextPath}/admin/category/add" class="btn btn-success"><i class="fa-solid fa-plus me-1"></i> Thêm Danh Mục Mới</a>
            </div>

            <!-- Search bar -->
            <form action="${pageContext.request.contextPath}/admin/categories" method="get" class="row g-2 mb-3">
                <div class="col-auto">
                    <input type="text" name="keyword" class="form-control" placeholder="Tìm kiếm theo tên danh mục..." value="${not empty keyword ? keyword : ''}">
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary"><i class="fa-solid fa-magnifying-glass me-1"></i> Tìm</button>
                    <c:if test="${not empty keyword}">
                        <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-outline-secondary">Xóa lọc</a>
                    </c:if>
                </div>
            </form>

            <div class="table-responsive">
                <table class="table table-hover table-bordered align-middle">
                    <thead class="table-dark text-center">
                        <tr>
                            <th style="width: 60px;">STT</th>
                            <th style="width: 160px;">Hình Ảnh</th>
                            <th>Tên Danh Mục</th>
                            <th style="width: 140px;">Trạng Thái</th>
                            <th style="width: 160px;">Hành Động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listcate}" var="cate" varStatus="STT">
                            <tr>
                                <td class="text-center fw-bold">${STT.index + 1}</td>
                                <c:choose>
                                    <c:when test="${not empty cate.images and (cate.images.startsWith('http://') or cate.images.startsWith('https://'))}">
                                        <c:url value="${cate.images}" var="imgUrl"></c:url>
                                    </c:when>
                                    <c:otherwise>
                                        <c:url value="/image?fname=${cate.images}" var="imgUrl"></c:url>
                                    </c:otherwise>
                                </c:choose>
                                <td class="text-center">
                                    <img height="90" width="130" src="${imgUrl}" alt="${cate.categoryname}" class="border bg-white p-1">
                                </td>
                                <td><span class="fw-semibold text-dark">${cate.categoryname}</span></td>
                                <td class="text-center">
                                    <c:if test="${cate.status == 1}">
                                        <span class="badge bg-success"><i class="fa-solid fa-check me-1"></i> Hoạt động</span>
                                    </c:if>
                                    <c:if test="${cate.status != 1}">
                                        <span class="badge bg-secondary"><i class="fa-solid fa-lock me-1"></i> Khóa</span>
                                    </c:if>
                                </td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/admin/category/edit?id=${cate.categoryId}" class="btn btn-sm btn-warning me-1">
                                        <i class="fa-solid fa-pen-to-square"></i> Sửa
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/category/delete?id=${cate.categoryId}" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc chắn muốn xóa danh mục này?');">
                                        <i class="fa-solid fa-trash"></i> Xóa
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
