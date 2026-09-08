package com.koha.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import com.koha.entity.Category;
import com.koha.entity.Product;
import com.koha.service.ICategoryService;
import com.koha.service.IProductService;
import com.koha.util.Constant;
import com.koha.util.ValidatorUtil;

@Controller
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping({"/products", "/product/list", "/product/search"})
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword,
                               Model model) {
        List<Product> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = productService.searchByName(keyword.trim());
            model.addAttribute("keyword", keyword.trim());
        } else {
            list = productService.findAll();
        }
        model.addAttribute("productList", list);
        return "admin/product-list";
    }

    @GetMapping("/product/add")
    public String addProductForm(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/product-add";
    }

    @PostMapping({"/product/insert", "/product/save"})
    public String insertProduct(
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "quantity", defaultValue = "0") Integer quantity,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "imageLink", required = false) String imageLink,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Map<String, String> errors = new HashMap<>();

        if (ValidatorUtil.isEmpty(productName)) {
            errors.put("productName", "Tên sản phẩm không được để trống!");
        } else if (!ValidatorUtil.isValidLength(productName, 2, 200)) {
            errors.put("productName", "Tên sản phẩm phải từ 2 đến 200 ký tự!");
        }

        Category category = null;
        if (categoryId == null || categoryId <= 0) {
            errors.put("categoryId", "Vui lòng chọn danh mục hợp lệ!");
        } else {
            category = categoryService.findById(categoryId);
            if (category == null) {
                errors.put("categoryId", "Danh mục đã chọn không tồn tại trong hệ thống!");
            }
        }

        if (price == null || price <= 0) {
            errors.put("price", "Đơn giá phải là số dương lớn hơn 0!");
        }

        if (quantity == null || quantity < 0) {
            errors.put("quantity", "Số lượng tồn kho phải là số nguyên không âm!");
        }

        if (status == null || (status != 0 && status != 1)) {
            status = 1;
        }

        if (ValidatorUtil.isNotEmpty(imageLink) && !ValidatorUtil.isValidImageUrl(imageLink)) {
            errors.put("imageLink", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String origName = imageFile.getOriginalFilename();
            if (origName != null && !ValidatorUtil.isValidImageExtension(origName)) {
                errors.put("imageFile", "Chỉ chấp nhận các định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp!");
            }
            if (imageFile.getSize() > 10 * 1024 * 1024) {
                errors.put("imageFile", "Dung lượng ảnh tối đa là 10MB!");
            }
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("oldName", productName);
            model.addAttribute("oldDescription", description);
            model.addAttribute("oldPrice", price);
            model.addAttribute("oldQuantity", quantity);
            model.addAttribute("oldStatus", status);
            model.addAttribute("oldCategoryId", categoryId);
            model.addAttribute("oldImageLink", imageLink);
            model.addAttribute("categories", categoryService.findAll());
            return "admin/product-add";
        }

        Product product = new Product();
        product.setProductName(productName.trim());
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

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String originalFileName = imageFile.getOriginalFilename();
                String extension = ".jpg";
                if (originalFileName != null && originalFileName.lastIndexOf('.') >= 0) {
                    extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
                }
                String newFileName = System.currentTimeMillis() + extension;
                imageFile.transferTo(new File(uploadDir, newFileName));
                product.setImages("product/" + newFileName);
            } else if (ValidatorUtil.isNotEmpty(imageLink)) {
                product.setImages(imageLink.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        productService.insert(product);
        session.setAttribute("message", "Thêm sản phẩm mới thành công!");
        redirectAttributes.addFlashAttribute("message", "Thêm sản phẩm mới thành công!");
        return "redirect:/admin/products";
    }

    @GetMapping("/product/edit")
    public String editProductForm(@RequestParam("id") Integer id, Model model) {
        if (id == null) {
            return "redirect:/admin/products";
        }
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/products";
        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "admin/product-edit";
    }

    @PostMapping("/product/update")
    public String updateProduct(
            @RequestParam("productId") Integer productId,
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "quantity", defaultValue = "0") Integer quantity,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "imageLink", required = false) String imageLink,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (productId == null) {
            return "redirect:/admin/products";
        }

        Product product = productService.findById(productId);
        if (product == null) {
            return "redirect:/admin/products";
        }

        Map<String, String> errors = new HashMap<>();

        if (ValidatorUtil.isEmpty(productName)) {
            errors.put("productName", "Tên sản phẩm không được để trống!");
        } else if (!ValidatorUtil.isValidLength(productName, 2, 200)) {
            errors.put("productName", "Tên sản phẩm phải từ 2 đến 200 ký tự!");
        }

        Category category = null;
        if (categoryId == null || categoryId <= 0) {
            errors.put("categoryId", "Vui lòng chọn danh mục hợp lệ!");
        } else {
            category = categoryService.findById(categoryId);
            if (category == null) {
                errors.put("categoryId", "Danh mục đã chọn không tồn tại trong hệ thống!");
            }
        }

        if (price == null || price <= 0) {
            errors.put("price", "Đơn giá phải là số dương lớn hơn 0!");
        }

        if (quantity == null || quantity < 0) {
            errors.put("quantity", "Số lượng tồn kho phải là số nguyên không âm!");
        }

        if (status == null || (status != 0 && status != 1)) {
            status = 1;
        }

        if (ValidatorUtil.isNotEmpty(imageLink) && !ValidatorUtil.isValidImageUrl(imageLink)) {
            errors.put("imageLink", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String origName = imageFile.getOriginalFilename();
            if (origName != null && !ValidatorUtil.isValidImageExtension(origName)) {
                errors.put("imageFile", "Chỉ chấp nhận các định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp!");
            }
            if (imageFile.getSize() > 10 * 1024 * 1024) {
                errors.put("imageFile", "Dung lượng ảnh tối đa là 10MB!");
            }
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            product.setProductName(productName);
            product.setDescription(description);
            if (price != null) product.setPrice(price);
            if (quantity != null) product.setQuantity(quantity);
            product.setStatus(status);
            if (category != null) product.setCategory(category);
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.findAll());
            return "admin/product-edit";
        }

        product.setProductName(productName.trim());
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

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String originalFileName = imageFile.getOriginalFilename();
                String extension = ".jpg";
                if (originalFileName != null && originalFileName.lastIndexOf('.') >= 0) {
                    extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
                }
                String newFileName = System.currentTimeMillis() + extension;
                imageFile.transferTo(new File(uploadDir, newFileName));
                product.setImages("product/" + newFileName);
            } else if (ValidatorUtil.isNotEmpty(imageLink)) {
                product.setImages(imageLink.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        productService.update(product);
        session.setAttribute("message", "Cập nhật thông tin sản phẩm thành công!");
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin sản phẩm thành công!");
        return "redirect:/admin/products";
    }

    @GetMapping("/product/delete")
    public String deleteProduct(@RequestParam("id") Integer id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (id != null) {
            try {
                productService.delete(id);
                session.setAttribute("message", "Đã xóa sản phẩm thành công!");
                redirectAttributes.addFlashAttribute("message", "Đã xóa sản phẩm thành công!");
            } catch (Exception e) {
                session.setAttribute("error", "Lỗi xóa sản phẩm: " + e.getMessage());
                redirectAttributes.addFlashAttribute("error", "Lỗi xóa sản phẩm: " + e.getMessage());
            }
        }
        return "redirect:/admin/products";
    }
}
