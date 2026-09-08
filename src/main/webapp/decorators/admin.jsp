<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title" /> - Quản Trị Hệ Thống</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <style>
        body {
            background-color: #f1f5f9;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .main-admin {
            flex: 1;
        }
    </style>
    <sitemesh:write property="head" />
</head>
<body>
    <!-- Header / Navbar Admin Bootstrap 5 -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold text-warning" href="<c:url value='/admin/products'/>">
                <i class="fa-solid fa-shield-halved me-2"></i>Koha Admin Panel
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#adminNavbar">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="adminNavbar">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/admin/products'/>">
                            <i class="fa-solid fa-boxes-stacked me-1"></i>Sản phẩm
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/admin/categories'/>">
                            <i class="fa-solid fa-layer-group me-1"></i>Danh mục
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-info" href="<c:url value='/home'/>">
                            <i class="fa-solid fa-arrow-up-right-from-square me-1"></i>Về Trang Khách
                        </a>
                    </li>
                </ul>

                <ul class="navbar-nav ms-auto align-items-center">
                    <li class="nav-item me-3 text-white-50">
                        Xin chào, <b class="text-white">${sessionScope.user.fullName}</b>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-outline-danger btn-sm" href="<c:url value='/logout'/>">
                            <i class="fa-solid fa-right-from-bracket me-1"></i>Đăng xuất
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Admin Main Body decorated by Sitemesh -->
    <main class="main-admin container py-4">
        <sitemesh:write property="body" />
    </main>

    <footer class="bg-secondary text-white py-3 mt-auto text-center small">
        Koha Admin Dashboard &copy; 2026 - Quản lý Danh mục & Sản phẩm
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

