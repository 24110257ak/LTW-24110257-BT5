<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thông báo lỗi</title>
</head>
<body style="font-family: Arial, sans-serif; margin: 40px;">
    <h2 style="color: red;">Đã xảy ra sự cố!</h2>
    <p>${not empty error ? error : 'Trang bạn yêu cầu không tồn tại hoặc đã xảy ra lỗi trong quá trình xử lý.'}</p>
    <br>
    <a href="${pageContext.request.contextPath}/home" style="display: inline-block; padding: 8px 16px; background-color: #6c757d; color: white; text-decoration: none; border-radius: 4px;">Về Trang Chủ</a>
</body>
</html>
