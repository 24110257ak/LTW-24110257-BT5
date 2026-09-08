<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title" /></title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .main-content {
            flex: 1;
        }
        .avatar-nav {
            width: 34px;
            height: 34px;
            object-fit: cover;
            border-radius: 50%;
            border: 2px solid #fff;
        }
    </style>
    <sitemesh:write property="head" />
</head>
<body>
    <!-- Header / Navbar Bootstrap 5 -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold" href="<c:url value='/home'/>">
                <i class="fa-solid fa-store me-2"></i>Koha Store
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="mainNavbar">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/home'/>">
                            <i class="fa-solid fa-house me-1"></i>Trang chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/product'/>">
                            <i class="fa-solid fa-box-open me-1"></i>Sản phẩm
                        </a>
                    </li>
                    <c:if test="${not empty sessionScope.user and sessionScope.user.roleid == 1}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle text-warning fw-semibold" href="#" data-bs-toggle="dropdown">
                                <i class="fa-solid fa-shield-halved me-1"></i>Quản trị
                            </a>
                            <ul class="dropdown-menu shadow">
                                <li>
                                    <a class="dropdown-item" href="<c:url value='/admin/products'/>">
                                        <i class="fa-solid fa-boxes-stacked me-2"></i>Quản lý sản phẩm
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="<c:url value='/admin/categories'/>">
                                        <i class="fa-solid fa-layer-group me-2"></i>Quản lý danh mục
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:if>
                </ul>

                <!-- User Profile / Auth Area -->
                <ul class="navbar-nav ms-auto align-items-center">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <c:set var="u" value="${sessionScope.user}" />
                            <c:choose>
                                <c:when test="${not empty u.images and (u.images.startsWith('http://') or u.images.startsWith('https://'))}">
                                    <c:url value="${u.images}" var="userAvatar" />
                                </c:when>
                                <c:when test="${not empty u.images}">
                                    <c:url value="/image?fname=${u.images}" var="userAvatar" />
                                </c:when>
                                <c:otherwise>
                                    <c:set var="userAvatar" value="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" />
                                </c:otherwise>
                            </c:choose>

                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle d-flex align-items-center text-white" href="#" data-bs-toggle="dropdown">
                                    <img src="${userAvatar}" alt="${u.fullName}" class="avatar-nav me-2" />
                                    <span>${u.fullName}</span>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end shadow">
                                    <li class="px-3 py-2 text-muted small border-bottom">
                                        Đăng nhập với <b>@${u.username}</b>
                                    </li>
                                    <li>
                                        <a class="dropdown-item py-2" href="<c:url value='/profile'/>">
                                            <i class="fa-solid fa-user-gear me-2 text-primary"></i>Hồ sơ cá nhân (Profile)
                                        </a>
                                    </li>
                                    <c:if test="${u.roleid == 1}">
                                        <li>
                                            <a class="dropdown-item py-2" href="<c:url value='/admin/products'/>">
                                                <i class="fa-solid fa-sliders me-2 text-warning"></i>Trang Quản Trị
                                            </a>
                                        </li>
                                    </c:if>
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item py-2 text-danger" href="<c:url value='/logout'/>">
                                            <i class="fa-solid fa-right-from-bracket me-2"></i>Đăng xuất
                                        </a>
                                    </li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item me-2">
                                <a class="btn btn-outline-light btn-sm" href="<c:url value='/login'/>">
                                    <i class="fa-solid fa-right-to-bracket me-1"></i>Đăng nhập
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-light btn-sm text-primary fw-bold" href="<c:url value='/register'/>">
                                    <i class="fa-solid fa-user-plus me-1"></i>Đăng ký (OTP)
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Body Section decorated by Sitemesh -->
    <main class="main-content container py-4">
        <sitemesh:write property="body" />
    </main>

    <!-- Footer Bootstrap 5 -->
    <footer class="bg-dark text-white py-4 mt-auto">
        <div class="container text-center">
            <p class="mb-1 fw-bold">Koha Web Store &copy; 2026 - Bài tập 2 + 3 + 4</p>
            <p class="mb-0 text-white-50 small">
                Hệ Thống Phân Tầng MVC - JPA 3.0 & Jakarta Servlet 6.0 - Quản lý giao diện bằng SiteMesh 3 Decorator
            </p>
        </div>
    </footer>

    <!-- Bootstrap 5 Bundle JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

