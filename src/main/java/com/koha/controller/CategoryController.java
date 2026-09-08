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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koha.entity.Category;
import com.koha.service.ICategoryService;
import com.koha.util.Constant;
import com.koha.util.ValidatorUtil;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private ICategoryService cateService;

    @GetMapping({"/categories", "/category/list"})
    public String listCategories(@RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "search", required = false) String search,
                                 Model model) {
        String kw = keyword;
        if (kw == null || kw.trim().isEmpty()) {
            kw = search;
        }

        List<Category> list;
        if (kw != null && !kw.trim().isEmpty()) {
            list = cateService.searchByName(kw.trim());
            model.addAttribute("keyword", kw.trim());
        } else {
            list = cateService.findAll();
        }

        model.addAttribute("listcate", list);
        model.addAttribute("cateList", list);
        return "admin/category-list";
    }

    @GetMapping("/category/add")
    public String addCategoryForm(Model model) {
        return "admin/category-add";
    }

    @PostMapping({"/category/insert", "/category/add", "/category/save"})
    public String saveCategory(@RequestParam(value = "categoryname", required = false) String categoryname,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "status", defaultValue = "1") int status,
                               @RequestParam(value = "images", required = false) String images,
                               @RequestParam(value = "images1", required = false) MultipartFile file1,
                               @RequestParam(value = "icon", required = false) MultipartFile iconFile,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        String cName = categoryname != null ? categoryname.trim() : (name != null ? name.trim() : "");
        Map<String, String> errors = new HashMap<>();

        if (ValidatorUtil.isEmpty(cName)) {
            errors.put("categoryname", "Tên danh mục không được để trống!");
        } else if (!ValidatorUtil.isValidLength(cName, 2, 100)) {
            errors.put("categoryname", "Tên danh mục phải từ 2 đến 100 ký tự!");
        } else if (cateService.findByCategoryname(cName) != null) {
            errors.put("categoryname", "Tên danh mục '" + cName + "' đã tồn tại!");
        }

        if (ValidatorUtil.isNotEmpty(images) && !ValidatorUtil.isValidImageUrl(images)) {
            errors.put("images", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
        }

        MultipartFile uploadFile = (file1 != null && !file1.isEmpty()) ? file1 : iconFile;
        if (uploadFile != null && !uploadFile.isEmpty()) {
            String origName = uploadFile.getOriginalFilename();
            if (origName != null && !ValidatorUtil.isValidImageExtension(origName)) {
                errors.put("imageFile", "Chỉ chấp nhận các định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp!");
            }
            if (uploadFile.getSize() > 10 * 1024 * 1024) {
                errors.put("imageFile", "Dung lượng ảnh tối đa là 10MB!");
            }
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("oldCategoryName", cName);
            model.addAttribute("oldStatus", status);
            model.addAttribute("oldImages", images);
            return "admin/category-add";
        }

        String fname = "";
        String uploadPath = Constant.CATEGORY_UPLOAD_DIR;
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            if (uploadFile != null && !uploadFile.isEmpty()) {
                String orig = uploadFile.getOriginalFilename();
                String ext = ".jpg";
                if (orig != null && orig.lastIndexOf(".") >= 0) {
                    ext = orig.substring(orig.lastIndexOf("."));
                }
                fname = "category_" + System.currentTimeMillis() + ext;
                uploadFile.transferTo(new File(dir, fname));
                fname = "category/" + fname;
            } else if (ValidatorUtil.isNotEmpty(images)) {
                fname = images.trim();
            } else {
                fname = "avatar.png";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Category category = new Category();
        category.setCategoryname(cName);
        category.setStatus(status);
        category.setImages(fname);

        cateService.insert(category);
        session.setAttribute("message", "Thêm danh mục mới thành công!");
        redirectAttributes.addFlashAttribute("message", "Thêm danh mục mới thành công!");
        return "redirect:/admin/categories";
    }

    @GetMapping({"/category/edit", "/category/edit/{id}"})
    public String editCategoryForm(@PathVariable(value = "id", required = false) Integer pathId,
                                   @RequestParam(value = "id", required = false) Integer paramId,
                                   Model model) {
        Integer id = pathId != null ? pathId : paramId;
        if (id == null) {
            return "redirect:/admin/categories";
        }

        Category category = cateService.findById(id);
        if (category == null) {
            return "redirect:/admin/categories";
        }

        model.addAttribute("category", category);
        model.addAttribute("cate", category);
        return "admin/category-edit";
    }

    @PostMapping({"/category/update", "/category/edit"})
    public String updateCategory(@RequestParam(value = "categoryid", required = false) Integer categoryid,
                                 @RequestParam(value = "id", required = false) Integer idParam,
                                 @RequestParam(value = "categoryname", required = false) String categoryname,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "status", defaultValue = "1") int status,
                                 @RequestParam(value = "images", required = false) String images,
                                 @RequestParam(value = "images1", required = false) MultipartFile file1,
                                 @RequestParam(value = "icon", required = false) MultipartFile iconFile,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        Integer id = categoryid != null ? categoryid : idParam;
        if (id == null) {
            return "redirect:/admin/categories";
        }

        Category category = cateService.findById(id);
        if (category == null) {
            return "redirect:/admin/categories";
        }

        String cName = categoryname != null ? categoryname.trim() : (name != null ? name.trim() : "");
        Map<String, String> errors = new HashMap<>();

        if (ValidatorUtil.isEmpty(cName)) {
            errors.put("categoryname", "Tên danh mục không được để trống!");
        } else if (!ValidatorUtil.isValidLength(cName, 2, 100)) {
            errors.put("categoryname", "Tên danh mục phải từ 2 đến 100 ký tự!");
        } else {
            Category existing = cateService.findByCategoryname(cName);
            if (existing != null && existing.getCategoryId() != id) {
                errors.put("categoryname", "Tên danh mục '" + cName + "' đã được sử dụng bởi danh mục khác!");
            }
        }

        if (ValidatorUtil.isNotEmpty(images) && !ValidatorUtil.isValidImageUrl(images)) {
            errors.put("images", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
        }

        MultipartFile uploadFile = (file1 != null && !file1.isEmpty()) ? file1 : iconFile;
        if (uploadFile != null && !uploadFile.isEmpty()) {
            String origName = uploadFile.getOriginalFilename();
            if (origName != null && !ValidatorUtil.isValidImageExtension(origName)) {
                errors.put("imageFile", "Chỉ chấp nhận các định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp!");
            }
            if (uploadFile.getSize() > 10 * 1024 * 1024) {
                errors.put("imageFile", "Dung lượng ảnh tối đa là 10MB!");
            }
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("category", category);
            model.addAttribute("cate", category);
            return "admin/category-edit";
        }

        try {
            if (uploadFile != null && !uploadFile.isEmpty()) {
                String orig = uploadFile.getOriginalFilename();
                String ext = ".jpg";
                if (orig != null && orig.lastIndexOf(".") >= 0) {
                    ext = orig.substring(orig.lastIndexOf("."));
                }
                String fname = "category_" + System.currentTimeMillis() + ext;
                File dir = new File(Constant.CATEGORY_UPLOAD_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                uploadFile.transferTo(new File(dir, fname));
                category.setImages("category/" + fname);
            } else if (ValidatorUtil.isNotEmpty(images)) {
                category.setImages(images.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        category.setCategoryname(cName);
        category.setStatus(status);

        cateService.update(category);
        session.setAttribute("message", "Cập nhật danh mục thành công!");
        redirectAttributes.addFlashAttribute("message", "Cập nhật danh mục thành công!");
        return "redirect:/admin/categories";
    }

    @GetMapping({"/category/delete", "/category/delete/{id}"})
    public String deleteCategory(@PathVariable(value = "id", required = false) Integer pathId,
                                 @RequestParam(value = "id", required = false) Integer paramId,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        Integer id = pathId != null ? pathId : paramId;
        if (id != null) {
            try {
                cateService.delete(id);
                session.setAttribute("message", "Xóa danh mục thành công!");
                redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công!");
            } catch (Exception e) {
                session.setAttribute("error", "Không thể xóa danh mục vì đang chứa sản phẩm liên kết!");
                redirectAttributes.addFlashAttribute("error", "Không thể xóa danh mục vì đang chứa sản phẩm liên kết!");
            }
        }
        return "redirect:/admin/categories";
    }
}

