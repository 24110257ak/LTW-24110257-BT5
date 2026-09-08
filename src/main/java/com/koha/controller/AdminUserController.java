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

import com.koha.entity.User;
import com.koha.service.IUserService;
import com.koha.util.Constant;
import com.koha.util.ValidatorUtil;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private IUserService userService;

    @GetMapping({"/users", "/user/list"})
    public String listUsers(@RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "search", required = false) String search,
                            Model model) {
        String kw = keyword;
        if (kw == null || kw.trim().isEmpty()) {
            kw = search;
        }

        List<User> list;
        if (kw != null && !kw.trim().isEmpty()) {
            list = userService.search(kw.trim());
            model.addAttribute("keyword", kw.trim());
        } else {
            list = userService.findAll();
        }

        model.addAttribute("users", list);
        return "admin/user-list";
    }

    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        return "admin/user-add";
    }

    @PostMapping({"/user/add", "/user/insert", "/user/save"})
    public String saveUser(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("fullname") String fullname,
                           @RequestParam(value = "email", required = false) String email,
                           @RequestParam(value = "phone", required = false) String phone,
                           @RequestParam(value = "roleid", defaultValue = "2") int roleid,
                           @RequestParam(value = "status", defaultValue = "1") int status,
                           @RequestParam(value = "images", required = false) String images,
                           @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                           Model model,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {
        Map<String, String> errors = new HashMap<>();

        String uName = username != null ? username.trim() : "";
        String pWord = password != null ? password.trim() : "";
        String fName = fullname != null ? fullname.trim() : "";
        String eMail = email != null ? email.trim() : "";
        String pNumber = phone != null ? phone.trim() : "";

        // Kiểm tra hợp lệ Tên đăng nhập
        if (ValidatorUtil.isEmpty(uName)) {
            errors.put("username", "Tên đăng nhập không được để trống!");
        } else if (!ValidatorUtil.isValidUsername(uName)) {
            errors.put("username", "Tên đăng nhập phải từ 3-30 ký tự, chỉ chứa chữ cái, số và dấu gạch dưới!");
        } else if (userService.checkExistUsername(uName)) {
            errors.put("username", "Tên đăng nhập '" + uName + "' đã được sử dụng!");
        }

        // Kiểm tra hợp lệ Mật khẩu
        if (ValidatorUtil.isEmpty(pWord)) {
            errors.put("password", "Mật khẩu không được để trống!");
        } else if (pWord.length() < 3) {
            errors.put("password", "Mật khẩu phải chứa ít nhất 3 ký tự!");
        }

        // Kiểm tra hợp lệ Họ tên
        if (ValidatorUtil.isEmpty(fName)) {
            errors.put("fullname", "Họ và tên không được để trống!");
        } else if (!ValidatorUtil.isValidLength(fName, 2, 100)) {
            errors.put("fullname", "Họ và tên phải từ 2 đến 100 ký tự!");
        }

        // Kiểm tra hợp lệ Email
        if (ValidatorUtil.isNotEmpty(eMail)) {
            if (!ValidatorUtil.isValidEmail(eMail)) {
                errors.put("email", "Địa chỉ email không đúng định dạng!");
            } else if (userService.checkExistEmail(eMail)) {
                errors.put("email", "Email '" + eMail + "' đã tồn tại trên hệ thống!");
            }
        }

        // Kiểm tra hợp lệ Số điện thoại
        if (ValidatorUtil.isNotEmpty(pNumber) && !ValidatorUtil.isValidPhone(pNumber)) {
            errors.put("phone", "Số điện thoại không hợp lệ (cần 10-11 chữ số)!");
        }

        // Kiểm tra đường dẫn ảnh nếu nhập URL
        if (ValidatorUtil.isNotEmpty(images) && !ValidatorUtil.isValidImageUrl(images)) {
            errors.put("images", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
        }

        // Kiểm tra file ảnh tải lên
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String origName = avatarFile.getOriginalFilename();
            if (origName != null && !ValidatorUtil.isValidImageExtension(origName)) {
                errors.put("avatarFile", "Chỉ chấp nhận các định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp!");
            }
            if (avatarFile.getSize() > 10 * 1024 * 1024) {
                errors.put("avatarFile", "Dung lượng ảnh tối đa là 10MB!");
            }
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("username", uName);
            model.addAttribute("fullname", fName);
            model.addAttribute("email", eMail);
            model.addAttribute("phone", pNumber);
            model.addAttribute("roleid", roleid);
            model.addAttribute("status", status);
            model.addAttribute("images", images);
            return "admin/user-add";
        }

        String finalImage = "";
        try {
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String orig = avatarFile.getOriginalFilename();
                String ext = ".jpg";
                if (orig != null && orig.lastIndexOf(".") >= 0) {
                    ext = orig.substring(orig.lastIndexOf("."));
                }
                String fname = "user_" + System.currentTimeMillis() + ext;
                File dir = new File(Constant.USER_UPLOAD_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                avatarFile.transferTo(new File(dir, fname));
                finalImage = "user/" + fname;
            } else if (ValidatorUtil.isNotEmpty(images)) {
                finalImage = images.trim();
            } else {
                finalImage = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        User newUser = new User();
        newUser.setUsername(uName);
        newUser.setPassword(pWord);
        newUser.setFullName(fName);
        newUser.setEmail(ValidatorUtil.isNotEmpty(eMail) ? eMail : null);
        newUser.setPhone(ValidatorUtil.isNotEmpty(pNumber) ? pNumber : null);
        newUser.setRoleid(roleid);
        newUser.setStatus(status);
        newUser.setImages(finalImage);

        userService.insert(newUser);

        session.setAttribute("message", "Thêm người dùng mới '" + uName + "' thành công!");
        redirectAttributes.addFlashAttribute("message", "Thêm người dùng mới '" + uName + "' thành công!");
        return "redirect:/admin/users";
    }

    @GetMapping({"/user/edit", "/user/edit/{id}"})
    public String editUserForm(@PathVariable(value = "id", required = false) Integer pathId,
                               @RequestParam(value = "id", required = false) Integer paramId,
                               Model model) {
        Integer id = pathId != null ? pathId : paramId;
        if (id == null) {
            return "redirect:/admin/users";
        }

        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }

        model.addAttribute("user", user);
        return "admin/user-edit";
    }

    @PostMapping({"/user/edit", "/user/update"})
    public String updateUser(@RequestParam("id") int id,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam("fullname") String fullname,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "roleid", defaultValue = "2") int roleid,
                             @RequestParam(value = "status", defaultValue = "1") int status,
                             @RequestParam(value = "images", required = false) String images,
                             @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            return "redirect:/admin/users";
        }

        Map<String, String> errors = new HashMap<>();

        String fName = fullname != null ? fullname.trim() : "";
        String eMail = email != null ? email.trim() : "";
        String pNumber = phone != null ? phone.trim() : "";

        if (ValidatorUtil.isEmpty(fName)) {
            errors.put("fullname", "Họ và tên không được để trống!");
        } else if (!ValidatorUtil.isValidLength(fName, 2, 100)) {
            errors.put("fullname", "Họ và tên phải từ 2 đến 100 ký tự!");
        }

        if (ValidatorUtil.isNotEmpty(eMail)) {
            if (!ValidatorUtil.isValidEmail(eMail)) {
                errors.put("email", "Địa chỉ email không đúng định dạng!");
            } else {
                User checkUser = userService.findByEmail(eMail);
                if (checkUser != null && checkUser.getId() != id) {
                    errors.put("email", "Email '" + eMail + "' đã được sử dụng bởi người dùng khác!");
                }
            }
        }

        if (ValidatorUtil.isNotEmpty(pNumber) && !ValidatorUtil.isValidPhone(pNumber)) {
            errors.put("phone", "Số điện thoại không hợp lệ (cần 10-11 chữ số)!");
        }

        if (password != null && !password.trim().isEmpty() && password.trim().length() < 3) {
            errors.put("password", "Mật khẩu mới phải chứa ít nhất 3 ký tự!");
        }

        if (ValidatorUtil.isNotEmpty(images) && !ValidatorUtil.isValidImageUrl(images)) {
            errors.put("images", "Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https://!");
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String origName = avatarFile.getOriginalFilename();
            if (origName != null && !ValidatorUtil.isValidImageExtension(origName)) {
                errors.put("avatarFile", "Chỉ chấp nhận các định dạng ảnh: .jpg, .jpeg, .png, .gif, .webp!");
            }
            if (avatarFile.getSize() > 10 * 1024 * 1024) {
                errors.put("avatarFile", "Dung lượng ảnh tối đa là 10MB!");
            }
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("user", existingUser);
            return "admin/user-edit";
        }

        // Cập nhật mật khẩu nếu có nhập mới
        if (password != null && !password.trim().isEmpty()) {
            existingUser.setPassword(password.trim());
        }

        existingUser.setFullName(fName);
        existingUser.setEmail(ValidatorUtil.isNotEmpty(eMail) ? eMail : null);
        existingUser.setPhone(ValidatorUtil.isNotEmpty(pNumber) ? pNumber : null);
        existingUser.setRoleid(roleid);
        existingUser.setStatus(status);

        // Xử lý avatar
        try {
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String orig = avatarFile.getOriginalFilename();
                String ext = ".jpg";
                if (orig != null && orig.lastIndexOf(".") >= 0) {
                    ext = orig.substring(orig.lastIndexOf("."));
                }
                String fname = "user_" + System.currentTimeMillis() + ext;
                File dir = new File(Constant.USER_UPLOAD_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                avatarFile.transferTo(new File(dir, fname));
                existingUser.setImages("user/" + fname);
            } else if (ValidatorUtil.isNotEmpty(images)) {
                existingUser.setImages(images.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        userService.update(existingUser);

        session.setAttribute("message", "Cập nhật người dùng '" + existingUser.getUsername() + "' thành công!");
        redirectAttributes.addFlashAttribute("message", "Cập nhật người dùng '" + existingUser.getUsername() + "' thành công!");
        return "redirect:/admin/users";
    }

    @GetMapping({"/user/delete", "/user/delete/{id}"})
    public String deleteUser(@PathVariable(value = "id", required = false) Integer pathId,
                             @RequestParam(value = "id", required = false) Integer paramId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Integer id = pathId != null ? pathId : paramId;
        if (id != null) {
            // Kiểm tra không được xóa chính tài khoản đang đăng nhập
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null && currentUser.getId() == id) {
                session.setAttribute("error", "Bạn không thể tự xóa tài khoản đang đăng nhập!");
                redirectAttributes.addFlashAttribute("error", "Bạn không thể tự xóa tài khoản đang đăng nhập!");
                return "redirect:/admin/users";
            }

            try {
                userService.delete(id);
                session.setAttribute("message", "Xóa người dùng thành công!");
                redirectAttributes.addFlashAttribute("message", "Xóa người dùng thành công!");
            } catch (Exception e) {
                session.setAttribute("error", "Lỗi khi xóa người dùng: " + e.getMessage());
                redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa người dùng: " + e.getMessage());
            }
        }
        return "redirect:/admin/users";
    }
}
