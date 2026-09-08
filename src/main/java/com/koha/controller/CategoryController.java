package com.koha.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
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
import com.koha.service.impl.CategoryServiceImpl;
import com.koha.util.Constant;
import com.koha.util.ValidatorUtil;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
@WebServlet(urlPatterns = {
    "/admin/categories",
    "/admin/category/list",
    "/admin/category/add",
    "/admin/category/insert",
    "/admin/category/edit",
    "/admin/category/update",
    "/admin/category/delete"
})
public class CategoryController extends HttpServlet {
import jakarta.servlet.http.HttpSession;

    private static final long serialVersionUID = 1L;
    public ICategoryService cateService = new CategoryServiceImpl();
@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
    @Autowired
    private ICategoryService cateService;

        if (url.contains("/admin/categories") || url.contains("/admin/category/list")) {
            String keyword = req.getParameter("keyword");
            if (keyword == null) {
                keyword = req.getParameter("search");
            }
    @GetMapping({"/categories", "/category/list"})
    public String listCategories(@RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "search", required = false) String search,
                                 Model model) {
        String kw = keyword;
        if (kw == null || kw.trim().isEmpty()) {
            kw = search;
        }

            List<Category> list;
            if (keyword != null && !keyword.trim().isEmpty()) {
                list = cateService.searchByName(keyword.trim());
                req.setAttribute("keyword", keyword.trim());
            } else {
                list = cateService.findAll();
            }
        List<Category> list;
        if (kw != null && !kw.trim().isEmpty()) {
            list = cateService.searchByName(kw.trim());
            model.addAttribute("keyword", kw.trim());
        } else {
            list = cateService.findAll();
        }

            req.setAttribute("listcate", list);
            req.setAttribute("cateList", list);
            req.getRequestDispatcher("/views/admin/category-list.jsp").forward(req, resp);
        model.addAttribute("listcate", list);
        model.addAttribute("cateList", list);
        return "admin/category-list";
    }

        } else if (url.contains("/admin/category/add")) {
            req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);
    @GetMapping("/category/add")
    public String addCategoryForm(Model model) {
        return "admin/category-add";
    }

        } else if (url.contains("/admin/category/edit")) {
            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    Category category = cateService.findById(id);
                    if (category != null) {
                        req.setAttribute("cate", category);
                        req.setAttribute("category", category);
                        req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
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

        } else if (url.contains("/admin/category/delete")) {
            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    cateService.delete(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        if (ValidatorUtil.isEmpty(cName)) {
            errors.put("categoryname", "Tên danh mục không được để trống!");
        } else if (!ValidatorUtil.isValidLength(cName, 2, 100)) {
            errors.put("categoryname", "Tên danh mục phải từ 2 đến 100 ký tự!");
        } else if (cateService.findByCategoryname(cName) != null) {
            errors.put("categoryname", "Tên danh mục '" + cName + "' đã tồn tại!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        if (ValidatorUtil.isNotEmpty(images) && !ValidatorUtil.isValidImageUrl(images)) {
            errors.put("images", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
        }

        if (url.contains("/admin/category/insert") || url.contains("/admin/category/add")) {
            String categoryname = req.getParameter("categoryname");
            if (categoryname == null) {
                categoryname = req.getParameter("name");
        MultipartFile uploadFile = (file1 != null && !file1.isEmpty()) ? file1 : iconFile;
        if (uploadFile != null && !uploadFile.isEmpty()) {
            String origName = uploadFile.getOriginalFilename();
            if (origName != null && !ValidatorUtil.isValidImageExtension(origName)) {
                errors.put("imageFile", "Chỉ chấp nhận các định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp!");
            }
            java.util.Map<String, String> errors = new java.util.HashMap<>();

            if (com.koha.util.ValidatorUtil.isEmpty(categoryname)) {
                errors.put("categoryname", "Tên danh mục không được để trống!");
            } else if (!com.koha.util.ValidatorUtil.isValidLength(categoryname, 2, 100)) {
                errors.put("categoryname", "Tên danh mục phải từ 2 đến 100 ký tự!");
            } else if (cateService.findByCategoryname(categoryname.trim()) != null) {
                errors.put("categoryname", "Tên danh mục '" + categoryname.trim() + "' đã tồn tại!");
            if (uploadFile.getSize() > 10 * 1024 * 1024) {
                errors.put("imageFile", "Dung lượng ảnh tối đa là 10MB!");
            }
        }

            String statusStr = req.getParameter("status");
            Integer status = com.koha.util.ValidatorUtil.parseIntSafe(statusStr);
            if (status == null || (status != 0 && status != 1)) {
                status = 1;
            }
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("oldCategoryName", cName);
            model.addAttribute("oldStatus", status);
            model.addAttribute("oldImages", images);
            return "admin/category-add";
        }

            String images = req.getParameter("images");
            if (com.koha.util.ValidatorUtil.isNotEmpty(images) && !com.koha.util.ValidatorUtil.isValidImageUrl(images)) {
                errors.put("images", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
            }
        String fname = "";
        String uploadPath = Constant.CATEGORY_UPLOAD_DIR;
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

            Part part = null;
            try {
                part = req.getPart("images1");
                if (part == null) {
                    part = req.getPart("icon");
        try {
            if (uploadFile != null && !uploadFile.isEmpty()) {
                String orig = uploadFile.getOriginalFilename();
                String ext = ".jpg";
                if (orig != null && orig.lastIndexOf(".") >= 0) {
                    ext = orig.substring(orig.lastIndexOf("."));
                }
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

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                req.setAttribute("oldCategoryName", categoryname);
                req.setAttribute("oldStatus", status);
                req.setAttribute("oldImages", images);
                req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);
                return;
            }
        Category category = new Category();
        category.setCategoryname(cName);
        category.setStatus(status);
        category.setImages(fname);

            String fname = "";
            String uploadPath = Constant.DIR;
        cateService.insert(category);
        session.setAttribute("message", "Thêm danh mục mới thành công!");
        redirectAttributes.addFlashAttribute("message", "Thêm danh mục mới thành công!");
        return "redirect:/admin/categories";
    }

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
    @GetMapping({"/category/edit", "/category/edit/{id}"})
    public String editCategoryForm(@PathVariable(value = "id", required = false) Integer pathId,
                                   @RequestParam(value = "id", required = false) Integer paramId,
                                   Model model) {
        Integer id = pathId != null ? pathId : paramId;
        if (id == null) {
            return "redirect:/admin/categories";
        }

            try {
                if (part != null && part.getSize() > 0 && part.getSubmittedFileName() != null && !part.getSubmittedFileName().trim().isEmpty()) {
                    String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    int index = filename.lastIndexOf(".");
                    String ext = index >= 0 ? filename.substring(index) : ".jpg";
                    fname = System.currentTimeMillis() + ext;
                    part.write(uploadPath + File.separator + fname);
                } else if (images != null && !images.trim().isEmpty()) {
                    fname = images.trim();
                } else {
                    fname = "avatar.png";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        Category category = cateService.findById(id);
        if (category == null) {
            return "redirect:/admin/categories";
        }

            Category category = new Category();
            category.setCategoryname(categoryname.trim());
            category.setStatus(status);
            category.setImages(fname);
        model.addAttribute("category", category);
        model.addAttribute("cate", category);
        return "admin/category-edit";
    }

            cateService.insert(category);
            req.getSession().setAttribute("message", "Thêm danh mục mới thành công!");
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
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

        } else if (url.contains("/admin/category/update") || url.contains("/admin/category/edit")) {
            String idStr = req.getParameter("categoryid");
            if (idStr == null) {
                idStr = req.getParameter("id");
            }
            Integer categoryid = com.koha.util.ValidatorUtil.parseIntSafe(idStr);
            if (categoryid == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/categories");
                return;
            }
        Category category = cateService.findById(id);
        if (category == null) {
            return "redirect:/admin/categories";
        }

            Category category = cateService.findById(categoryid);
            if (category == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/categories");
                return;
            }
        String cName = categoryname != null ? categoryname.trim() : (name != null ? name.trim() : "");
        Map<String, String> errors = new HashMap<>();

            String categoryname = req.getParameter("categoryname");
            if (categoryname == null) {
                categoryname = req.getParameter("name");
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

            java.util.Map<String, String> errors = new java.util.HashMap<>();
        if (ValidatorUtil.isNotEmpty(images) && !ValidatorUtil.isValidImageUrl(images)) {
            errors.put("images", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
        }

            if (com.koha.util.ValidatorUtil.isEmpty(categoryname)) {
                errors.put("categoryname", "Tên danh mục không được để trống!");
            } else if (!com.koha.util.ValidatorUtil.isValidLength(categoryname, 2, 100)) {
                errors.put("categoryname", "Tên danh mục phải từ 2 đến 100 ký tự!");
            } else {
                Category exist = cateService.findByCategoryname(categoryname.trim());
                if (exist != null && exist.getCategoryId() != categoryid) {
                    errors.put("categoryname", "Tên danh mục '" + categoryname.trim() + "' đã tồn tại!");
                }
        MultipartFile uploadFile = (file1 != null && !file1.isEmpty()) ? file1 : iconFile;
        if (uploadFile != null && !uploadFile.isEmpty()) {
            String origName = uploadFile.getOriginalFilename();
            if (origName != null && !ValidatorUtil.isValidImageExtension(origName)) {
                errors.put("imageFile", "Chỉ chấp nhận các định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp!");
            }

            String statusStr = req.getParameter("status");
            Integer status = com.koha.util.ValidatorUtil.parseIntSafe(statusStr);
            if (status == null || (status != 0 && status != 1)) {
                status = 1;
            if (uploadFile.getSize() > 10 * 1024 * 1024) {
                errors.put("imageFile", "Dung lượng ảnh tối đa là 10MB!");
            }
        }

            String images = req.getParameter("images");
            if (com.koha.util.ValidatorUtil.isNotEmpty(images) && !com.koha.util.ValidatorUtil.isValidImageUrl(images)) {
                errors.put("images", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
            }
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("category", category);
            model.addAttribute("cate", category);
            return "admin/category-edit";
        }

            Part part = null;
            try {
                part = req.getPart("images1");
                if (part == null) {
                    part = req.getPart("icon");
        try {
            if (uploadFile != null && !uploadFile.isEmpty()) {
                String orig = uploadFile.getOriginalFilename();
                String ext = ".jpg";
                if (orig != null && orig.lastIndexOf(".") >= 0) {
                    ext = orig.substring(orig.lastIndexOf("."));
                }
                if (part != null && part.getSize() > 0) {
                    String subName = part.getSubmittedFileName();
                    if (subName != null && !com.koha.util.ValidatorUtil.isValidImageExtension(subName)) {
                        errors.put("imageFile", "Chỉ chấp nhận các định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp!");
                    }
                    if (part.getSize() > 10 * 1024 * 1024) {
                        errors.put("imageFile", "Dung lượng ảnh tối đa là 10MB!");
                    }
                String fname = "category_" + System.currentTimeMillis() + ext;
                File dir = new File(Constant.CATEGORY_UPLOAD_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            } catch (Exception e) {
                errors.put("imageFile", "Lỗi tiếp nhận file ảnh: " + e.getMessage());
                uploadFile.transferTo(new File(dir, fname));
                category.setImages("category/" + fname);
            } else if (ValidatorUtil.isNotEmpty(images)) {
                category.setImages(images.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                category.setCategoryname(categoryname);
                category.setStatus(status);
                req.setAttribute("category", category);
                req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
                return;
            }
        category.setCategoryname(cName);
        category.setStatus(status);

            String fileold = category.getImages();
            String fname = fileold;
            String uploadPath = Constant.DIR;
        cateService.update(category);
        session.setAttribute("message", "Cập nhật danh mục thành công!");
        redirectAttributes.addFlashAttribute("message", "Cập nhật danh mục thành công!");
        return "redirect:/admin/categories";
    }

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

    @GetMapping({"/category/delete", "/category/delete/{id}"})
    public String deleteCategory(@PathVariable(value = "id", required = false) Integer pathId,
                                 @RequestParam(value = "id", required = false) Integer paramId,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        Integer id = pathId != null ? pathId : paramId;
        if (id != null) {
            try {
                if (part != null && part.getSize() > 0 && part.getSubmittedFileName() != null && !part.getSubmittedFileName().trim().isEmpty()) {
                    // Xóa file cũ trên thư mục nếu không phải URL online
                    if (fileold != null && !fileold.startsWith("http") && !fileold.equals("avatar.png")) {
                        try {
                            deleteFile(uploadPath + File.separator + fileold);
                        } catch (Exception ex) {
                            // Bỏ qua nếu file không tồn tại
                        }
                    }

                    String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    int index = filename.lastIndexOf(".");
                    String ext = index >= 0 ? filename.substring(index) : ".jpg";
                    fname = System.currentTimeMillis() + ext;
                    part.write(uploadPath + File.separator + fname);
                } else if (images != null && !images.trim().isEmpty()) {
                    fname = images.trim();
                }
                cateService.delete(id);
                session.setAttribute("message", "Xóa danh mục thành công!");
                redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("error", "Không thể xóa danh mục vì đang chứa sản phẩm liên kết!");
                redirectAttributes.addFlashAttribute("error", "Không thể xóa danh mục vì đang chứa sản phẩm liên kết!");
            }

            category.setCategoryname(categoryname.trim());
            category.setStatus(status);
            category.setImages(fname);

            cateService.update(category);
            req.getSession().setAttribute("message", "Cập nhật danh mục thành công!");
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        }
        return "redirect:/admin/categories";
    }

    public static void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }
}

