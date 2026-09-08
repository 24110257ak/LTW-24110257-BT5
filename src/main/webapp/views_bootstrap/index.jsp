<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ - Bootstrap Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>body { background-color: #f4f6f9; }</style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-5 shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-graduation-cap me-2"></i>JPA 3.0 & Servlet 6.0 Project</a>
        </div>
    </nav>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-sm border-0 p-4">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user or not empty sessionScope.account}">
                            <c:set var="u" value="${not empty sessionScope.user ? sessionScope.user : sessionScope.account}"/>
                            <div class="d-flex align-items-center mb-4">
                                <div class="bg-primary text-white rounded-circle p-3 me-3 fs-3">
                                    <i class="fa-solid fa-user-check"></i>
                                </div>
                                <div>
                                    <h3 class="mb-0 fw-bold">Xin chào, ${u.fullName}!</h3>
                                    <p class="text-muted mb-0">Tài khoản: <code>${u.username}</code> | Quyền: <span class="badge bg-info text-dark">${u.roleid == 1 ? 'Quản Trị Viên' : 'Người Dùng'}</span></p>
                                </div>
                            </div>

                            <div class="list-group mb-4">
                                <a href="${pageContext.request.contextPath}/admin/categories" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center py-3">
                                    <div>
                                        <h6 class="mb-1 fw-bold text-primary"><i class="fa-solid fa-table-list me-2"></i>Quản Lý Danh Mục (JPA CRUD)</h6>
                                        <small class="text-muted">Xem, thêm mới, chỉnh sửa, xóa danh mục và tải ảnh</small>
                                    </div>
                                    <i class="fa-solid fa-chevron-right text-muted"></i>
                                </a>
                            </div>

                            <div>
                                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger"><i class="fa-solid fa-right-from-bracket me-1"></i> Đăng Xuất (Hủy Session)</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-4">
                                <div class="mb-3 text-secondary fs-1"><i class="fa-solid fa-user-lock"></i></div>
                                <h4 class="fw-bold mb-2">Bạn chưa đăng nhập</h4>
                                <p class="text-muted mb-4">Vui lòng đăng nhập để trải nghiệm đầy đủ các tính năng quản lý danh mục với JPA.</p>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary px-4 py-2 fw-semibold"><i class="fa-solid fa-arrow-right-to-bracket me-1"></i> Đăng Nhập Ngay</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
