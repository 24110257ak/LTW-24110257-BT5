<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh Sửa Danh Mục - Bootstrap</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>body { background-color: #f4f6f9; }</style>
</head>
<body>
    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-7">
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-warning text-dark py-3">
                        <h5 class="mb-0 fw-bold"><i class="fa-solid fa-pen-to-square me-2"></i>Chỉnh Sửa Danh Mục (JPA)</h5>
                    </div>
                    <div class="card-body p-4">
                        <form action="${pageContext.request.contextPath}/admin/category/update" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="categoryid" value="${cate.categoryId}">

                            <div class="mb-3">
                                <label for="categoryname" class="form-label fw-bold">Tên danh mục <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="categoryname" name="categoryname" value="${cate.categoryname}" required>
                            </div>

                            <div class="mb-3">
                                <label for="images" class="form-label fw-bold">Link hình ảnh (Online URL):</label>
                                <input type="text" class="form-control" id="images" name="images" value="${cate.images}">
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold d-block">Hình ảnh hiện tại:</label>
                                <c:choose>
                                    <c:when test="${not empty cate.images and (cate.images.startsWith('http://') or cate.images.startsWith('https://'))}">
                                        <c:url value="${cate.images}" var="imgUrl"></c:url>
                                    </c:when>
                                    <c:otherwise>
                                        <c:url value="/image?fname=${cate.images}" var="imgUrl"></c:url>
                                    </c:otherwise>
                                </c:choose>
                                <img height="100" width="140" src="${imgUrl}" alt="${cate.categoryname}" class="border rounded bg-white p-1 mb-2" style="object-fit: contain;">
                            </div>

                            <div class="mb-3">
                                <label for="images1" class="form-label fw-bold">Upload hình ảnh mới (nếu muốn thay đổi):</label>
                                <input type="file" class="form-control" id="images1" name="images1">
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-bold d-block">Trạng thái:</label>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="status" id="status1" value="1" ${cate.status == 1 ? 'checked' : ''}>
                                    <label class="form-check-label text-success fw-semibold" for="status1"><i class="fa-solid fa-check"></i> Hoạt động</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="status" id="status0" value="0" ${cate.status != 1 ? 'checked' : ''}>
                                    <label class="form-check-label text-secondary fw-semibold" for="status0"><i class="fa-solid fa-lock"></i> Khóa</label>
                                </div>
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-outline-secondary"><i class="fa-solid fa-arrow-left me-1"></i> Quay lại</a>
                                <div>
                                    <button type="submit" class="btn btn-warning fw-bold"><i class="fa-solid fa-check me-1"></i> Cập Nhật</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
