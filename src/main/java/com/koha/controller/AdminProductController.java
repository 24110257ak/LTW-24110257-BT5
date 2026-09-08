package com.koha.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import com.koha.entity.Category;
import com.koha.entity.Product;
import com.koha.service.ICategoryService;
import com.koha.service.IProductService;
import com.koha.service.impl.CategoryServiceImpl;
import com.koha.service.impl.ProductServiceImpl;
import com.koha.util.Constant;

@WebServlet(urlPatterns = {
    "/admin/products",
    "/admin/product/list",
    "/admin/product/add",
    "/admin/product/insert",
    "/admin/product/edit",
    "/admin/product/update",
    "/admin/product/delete",
    "/admin/product/search"
})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class AdminProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IProductService productService = new ProductServiceImpl();
    private ICategoryService categoryService = new CategoryServiceImpl();

    /**
     * Phương thức trích xuất tên file upload theo đúng chuẩn slide giáo trình 10_UploadFile_servlet_jakarta.pdf
     */
    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                String fileName = content.substring(content.indexOf("=") + 2, content.length() - 1);
                // Xử lý lấy tên file thuần túy nếu có đường dẫn tuyệt đối từ client IE/Windows
                return Paths.get(fileName).getFileName().toString();
            }
        }
        return Constant.DEFAULT_FILENAME;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();

        if (url.contains("/admin/product/add")) {
            List<Category> categories = categoryService.findAll();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/views/admin/product-add.jsp").forward(req, resp);

        } else if (url.contains("/admin/product/edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Product product = productService.findById(id);
            List<Category> categories = categoryService.findAll();
            req.setAttribute("product", product);
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);

        } else if (url.contains("/admin/product/delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            try {
                productService.delete(id);
                req.getSession().setAttribute("message", "Đã xóa sản phẩm thành công!");
            } catch (Exception e) {
                req.getSession().setAttribute("error", "Lỗi xóa sản phẩm: " + e.getMessage());
            }
            resp.sendRedirect(req.getContextPath() + "/admin/products");

        } else {
            // Mặc định hiển thị danh sách sản phẩm
            String keyword = req.getParameter("keyword");
            List<Product> list;
            if (keyword != null && !keyword.trim().isEmpty()) {
                list = productService.searchByName(keyword.trim());
                req.setAttribute("keyword", keyword.trim());
            } else {
                list = productService.findAll();
            }
            req.setAttribute("productList", list);
            req.getRequestDispatcher("/views/admin/product-list.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        if (url.contains("/admin/product/insert")) {
            String name = req.getParameter("productName");
            String description = req.getParameter("description");
            String priceStr = req.getParameter("price");
            String quantityStr = req.getParameter("quantity");
            String statusStr = req.getParameter("status");
            String categoryIdStr = req.getParameter("categoryId");
            String imageLink = req.getParameter("imageLink");

            java.util.Map<String, String> errors = new java.util.HashMap<>();

            if (com.koha.util.ValidatorUtil.isEmpty(name)) {
                errors.put("productName", "Tên sản phẩm không được để trống!");
            } else if (!com.koha.util.ValidatorUtil.isValidLength(name, 2, 200)) {
                errors.put("productName", "Tên sản phẩm phải từ 2 đến 200 ký tự!");
            }

            Integer categoryId = com.koha.util.ValidatorUtil.parseIntSafe(categoryIdStr);
            Category category = null;
            if (categoryId == null || categoryId <= 0) {
                errors.put("categoryId", "Vui lòng chọn danh mục hợp lệ!");
            } else {
                category = categoryService.findById(categoryId);
                if (category == null) {
                    errors.put("categoryId", "Danh mục đã chọn không tồn tại trong hệ thống!");
                }
            }

            Double price = com.koha.util.ValidatorUtil.parseDoubleSafe(priceStr);
            if (price == null || price <= 0) {
                errors.put("price", "Đơn giá phải là số dương lớn hơn 0!");
            }

            Integer quantity = com.koha.util.ValidatorUtil.parseIntSafe(quantityStr);
            if (quantity == null || quantity < 0) {
                errors.put("quantity", "Số lượng tồn kho phải là số nguyên không âm (>= 0)!");
            }

            Integer status = com.koha.util.ValidatorUtil.parseIntSafe(statusStr);
            if (status == null || (status != 0 && status != 1)) {
                status = 1;
            }

            if (com.koha.util.ValidatorUtil.isNotEmpty(imageLink) && !com.koha.util.ValidatorUtil.isValidImageUrl(imageLink)) {
                errors.put("imageLink", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
            }

            Part part = null;
            try {
                part = req.getPart("imageFile");
                if (part != null && part.getSize() > 0) {
                    String subName = part.getSubmittedFileName();
                    if (subName != null && !com.koha.util.ValidatorUtil.isValidImageExtension(subName)) {
                        errors.put("imageFile", "Chỉ chấp nhận các định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp!");
                    }
                    if (part.getSize() > 10 * 1024 * 1024) {
                        errors.put("imageFile", "Dung lượng ảnh tối đa là 10MB!");
                    }
                }
            } catch (Exception e) {
                errors.put("imageFile", "Lỗi tiếp nhận file ảnh: " + e.getMessage());
            }

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                req.setAttribute("oldName", name);
                req.setAttribute("oldDescription", description);
                req.setAttribute("oldPrice", priceStr);
                req.setAttribute("oldQuantity", quantityStr);
                req.setAttribute("oldStatus", status);
                req.setAttribute("oldCategoryId", categoryIdStr);
                req.setAttribute("oldImageLink", imageLink);
                req.setAttribute("categories", categoryService.findAll());
                req.getRequestDispatcher("/views/admin/product-add.jsp").forward(req, resp);
                return;
            }

            Product product = new Product();
            product.setProductName(name.trim());
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setStatus(status);
            product.setCategory(category);

            // Xử lý upload file hình ảnh theo chuẩn Multipart
            String uploadPath = Constant.PRODUCT_UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            if (part != null && part.getSize() > 0) {
                String originalFileName = getFileName(part);
                if (!originalFileName.equals(Constant.DEFAULT_FILENAME) && !originalFileName.trim().isEmpty()) {
                    String extension = "";
                    int i = originalFileName.lastIndexOf('.');
                    if (i > 0) {
                        extension = originalFileName.substring(i);
                    }
                    String newFileName = System.currentTimeMillis() + extension;
                    part.write(uploadPath + File.separator + newFileName);
                    product.setImages("product/" + newFileName);
                }
            } else if (imageLink != null && !imageLink.trim().isEmpty()) {
                product.setImages(imageLink.trim());
            }

            productService.insert(product);
            req.getSession().setAttribute("message", "Thêm sản phẩm mới thành công!");
            resp.sendRedirect(req.getContextPath() + "/admin/products");

        } else if (url.contains("/admin/product/update")) {
            String idStr = req.getParameter("productId");
            Integer id = com.koha.util.ValidatorUtil.parseIntSafe(idStr);
            if (id == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/products");
                return;
            }

            Product product = productService.findById(id);
            if (product == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/products");
                return;
            }

            String name = req.getParameter("productName");
            String description = req.getParameter("description");
            String priceStr = req.getParameter("price");
            String quantityStr = req.getParameter("quantity");
            String statusStr = req.getParameter("status");
            String categoryIdStr = req.getParameter("categoryId");
            String imageLink = req.getParameter("imageLink");

            java.util.Map<String, String> errors = new java.util.HashMap<>();

            if (com.koha.util.ValidatorUtil.isEmpty(name)) {
                errors.put("productName", "Tên sản phẩm không được để trống!");
            } else if (!com.koha.util.ValidatorUtil.isValidLength(name, 2, 200)) {
                errors.put("productName", "Tên sản phẩm phải từ 2 đến 200 ký tự!");
            }

            Integer categoryId = com.koha.util.ValidatorUtil.parseIntSafe(categoryIdStr);
            Category category = null;
            if (categoryId == null || categoryId <= 0) {
                errors.put("categoryId", "Vui lòng chọn danh mục hợp lệ!");
            } else {
                category = categoryService.findById(categoryId);
                if (category == null) {
                    errors.put("categoryId", "Danh mục đã chọn không tồn tại trong hệ thống!");
                }
            }

            Double price = com.koha.util.ValidatorUtil.parseDoubleSafe(priceStr);
            if (price == null || price <= 0) {
                errors.put("price", "Đơn giá phải là số dương lớn hơn 0!");
            }

            Integer quantity = com.koha.util.ValidatorUtil.parseIntSafe(quantityStr);
            if (quantity == null || quantity < 0) {
                errors.put("quantity", "Số lượng tồn kho phải là số nguyên không âm (>= 0)!");
            }

            Integer status = com.koha.util.ValidatorUtil.parseIntSafe(statusStr);
            if (status == null || (status != 0 && status != 1)) {
                status = 1;
            }

            if (com.koha.util.ValidatorUtil.isNotEmpty(imageLink) && !com.koha.util.ValidatorUtil.isValidImageUrl(imageLink)) {
                errors.put("imageLink", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
            }

            Part part = null;
            try {
                part = req.getPart("imageFile");
                if (part != null && part.getSize() > 0) {
                    String subName = part.getSubmittedFileName();
                    if (subName != null && !com.koha.util.ValidatorUtil.isValidImageExtension(subName)) {
                        errors.put("imageFile", "Chỉ chấp nhận các định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp!");
                    }
                    if (part.getSize() > 10 * 1024 * 1024) {
                        errors.put("imageFile", "Dung lượng ảnh tối đa là 10MB!");
                    }
                }
            } catch (Exception e) {
                errors.put("imageFile", "Lỗi tiếp nhận file ảnh: " + e.getMessage());
            }

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                product.setProductName(name);
                product.setDescription(description);
                if (price != null) product.setPrice(price);
                if (quantity != null) product.setQuantity(quantity);
                product.setStatus(status);
                if (category != null) product.setCategory(category);
                req.setAttribute("product", product);
                req.setAttribute("categories", categoryService.findAll());
                req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);
                return;
            }

            product.setProductName(name.trim());
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setStatus(status);
            product.setCategory(category);

            String uploadPath = Constant.PRODUCT_UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            if (part != null && part.getSize() > 0) {
                String originalFileName = getFileName(part);
                if (!originalFileName.equals(Constant.DEFAULT_FILENAME) && !originalFileName.trim().isEmpty()) {
                    String extension = "";
                    int i = originalFileName.lastIndexOf('.');
                    if (i > 0) {
                        extension = originalFileName.substring(i);
                    }
                    String newFileName = System.currentTimeMillis() + extension;
                    part.write(uploadPath + File.separator + newFileName);
                    product.setImages("product/" + newFileName);
                }
            } else if (imageLink != null && !imageLink.trim().isEmpty()) {
                product.setImages(imageLink.trim());
            }

            productService.update(product);
            req.getSession().setAttribute("message", "Cập nhật thông tin sản phẩm thành công!");
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        }
    }
}
