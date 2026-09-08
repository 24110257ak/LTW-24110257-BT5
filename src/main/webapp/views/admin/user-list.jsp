<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Người Dùng - Admin Panel</title>
</head>
<body>
    <!-- Title & Add Button -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h3 class="fw-bold text-dark mb-1">
                <i class="fa-solid fa-users text-primary me-2"></i>Quản Lý Người Dùng
            </h3>
            <p class="text-muted small mb-0">Hệ thống phân quyền &amp; quản trị tài khoản Spring Boot 3</p>
        </div>
        <div>
            <a href="<c:url value='/admin/user/add'/>" class="btn btn-success shadow-sm">
                <i class="fa-solid fa-user-plus me-1"></i>Thêm Người Dùng Mới
            </a>
        </div>
    </div>

    <!-- Thông báo kết quả -->
    <c:if test="${not empty message or not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="fa-solid fa-circle-check me-2"></i>${not empty message ? message : sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="message" scope="session"/>
    </c:if>
    <c:if test="${not empty error or not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
            <i class="fa-solid fa-triangle-exclamation me-2"></i>${not empty error ? error : sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <!-- Search bar -->
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body p-3">
            <form action="<c:url value='/admin/users'/>" method="get" class="row g-2 align-items-center">
                <div class="col-md-5">
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="fa-solid fa-magnifying-glass"></i></span>
                        <input type="text" name="keyword" class="form-control" 
                               value="${not empty keyword ? keyword : ''}" 
                               placeholder="Tìm theo Username, Họ tên, Email, SĐT...">
                    </div>
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary shadow-sm">
                        <i class="fa-solid fa-magnifying-glass me-1"></i>Tìm kiếm
                    </button>
                    <c:if test="${not empty keyword}">
                        <a href="<c:url value='/admin/users'/>" class="btn btn-outline-secondary ms-1">
                            <i class="fa-solid fa-xmark me-1"></i>Xóa lọc
                        </a>
                    </c:if>
                </div>
            </form>
        </div>
    </div>

    <!-- User Table -->
    <div class="card border-0 shadow-sm">
        <div class="card-header bg-white py-3">
            <h6 class="fw-bold mb-0 text-secondary">
                <i class="fa-solid fa-list me-2 text-primary"></i>Danh Sách Tài Khoản (${users.size()})
            </h6>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover table-striped align-middle mb-0">
                    <thead class="table-light text-center">
                        <tr>
                            <th style="width: 50px;">STT</th>
                            <th style="width: 70px;">Avatar</th>
                            <th class="text-start" style="width: 150px;">Tên đăng nhập</th>
                            <th class="text-start">Họ và tên</th>
                            <th class="text-start">Liên hệ (Email / SĐT)</th>
                            <th style="width: 120px;">Vai trò</th>
                            <th style="width: 130px;">Trạng thái</th>
                            <th style="width: 140px;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${users}" var="u" varStatus="stt">
                            <c:choose>
                                <c:when test="${not empty u.images and (u.images.startsWith('http://') or u.images.startsWith('https://'))}">
                                    <c:url value="${u.images}" var="avatarUrl" />
                                </c:when>
                                <c:when test="${not empty u.images}">
                                    <c:url value="/image?fname=${u.images}" var="avatarUrl" />
                                </c:when>
                                <c:otherwise>
                                    <c:url value="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" var="avatarUrl" />
                                </c:otherwise>
                            </c:choose>

                            <tr>
                                <td class="text-center fw-bold">${stt.index + 1}</td>
                                <td class="text-center">
                                    <img src="${avatarUrl}" alt="${u.username}" width="45" height="45" 
                                         class="rounded-circle border shadow-sm object-fit-cover">
                                </td>
                                <td>
                                    <span class="fw-bold text-dark">${u.username}</span>
                                    <div class="text-muted small">ID: #${u.id}</div>
                                </td>
                                <td>
                                    <span class="fw-semibold">${u.fullName}</span>
                                </td>
                                <td>
                                    <div>
                                        <i class="fa-solid fa-envelope text-muted me-1 small"></i>
                                        <span class="small">${not empty u.email ? u.email : '<em class="text-muted">Chưa có</em>'}</span>
                                    </div>
                                    <div>
                                        <i class="fa-solid fa-phone text-muted me-1 small"></i>
                                        <span class="small">${not empty u.phone ? u.phone : '<em class="text-muted">Chưa có</em>'}</span>
                                    </div>
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${u.roleid == 1}">
                                            <span class="badge bg-danger shadow-sm px-2 py-1">
                                                <i class="fa-solid fa-shield-halved me-1"></i>Admin
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-info text-dark shadow-sm px-2 py-1">
                                                <i class="fa-solid fa-user me-1"></i>User
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${u.status == 1}">
                                            <span class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1">
                                                <i class="fa-solid fa-circle-check me-1"></i>Kích hoạt
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary-subtle text-secondary border border-secondary-subtle px-2 py-1">
                                                <i class="fa-solid fa-circle-pause me-1"></i>Chưa kích hoạt
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <a href="<c:url value='/admin/user/edit?id=${u.id}'/>" 
                                       class="btn btn-outline-warning btn-sm shadow-sm me-1" 
                                       title="Chỉnh sửa">
                                        <i class="fa-solid fa-pen-to-square"></i>
                                    </a>
                                    <a href="<c:url value='/admin/user/delete?id=${u.id}'/>" 
                                       class="btn btn-outline-danger btn-sm shadow-sm" 
                                       title="Xóa"
                                       onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng [${u.username}] không?');">
                                        <i class="fa-solid fa-trash"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty users}">
                            <tr>
                                <td colspan="8" class="text-center py-5 text-muted">
                                    <i class="fa-solid fa-user-slash fs-1 d-block mb-3 text-secondary opacity-50"></i>
                                    Không tìm thấy người dùng nào phù hợp với từ khóa!
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

