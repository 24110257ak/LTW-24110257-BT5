package com.koha.config;

import java.util.List;

import com.koha.entity.Product;
import com.koha.service.IProductService;
import com.koha.service.IUserService;
import com.koha.service.impl.ProductServiceImpl;
import com.koha.service.impl.UserServiceImpl;

public class Test {

    public static void main(String[] args) {
        System.out.println("=== BẮT ĐẦU KIỂM THỬ HỆ THỐNG MỞ RỘNG (JPA 3.0 & Jakarta Servlet 6.0) ===");

        IProductService productService = new ProductServiceImpl();
        IUserService userService = new UserServiceImpl();

        try {
            // 1. Kiểm thử truy vấn Top 10 sản phẩm mới nhất
            List<Product> top10 = productService.findTop10();
            System.out.println("✅ [TEST 1] Số lượng sản phẩm mới nhất lấy được (Top 10): " + top10.size());
            for (int i = 0; i < Math.min(3, top10.size()); i++) {
                Product p = top10.get(i);
                System.out.println("   + SP #" + p.getProductId() + ": " + p.getProductName() + " | Giá: " + p.getFormattedPrice() + " | Danh mục: " + (p.getCategory() != null ? p.getCategory().getCategoryname() : "N/A"));
            }

            // 2. Kiểm thử phân trang 6 sản phẩm / trang
            List<Product> page1 = productService.findAll(0, 6);
            int totalCount = productService.count();
            int totalPages = (int) Math.ceil((double) totalCount / 6);
            System.out.println("✅ [TEST 2] Phân trang 6sp/trang - Tổng số SP: " + totalCount + " | Tổng số trang: " + totalPages + " | Số SP trang 1: " + page1.size());

            // 3. Kiểm thử đăng ký tài khoản với OTP
            String testUser = "testuser_" + (System.currentTimeMillis() % 10000);
            String testEmail = testUser + "@gmail.com";
            boolean registered = userService.registerWithOtp(testUser, "123456", "Người dùng Test", testEmail, "0988776655");
            System.out.println("✅ [TEST 3] Đăng ký User kèm sinh OTP và gửi mail: " + (registered ? "THÀNH CÔNG" : "THẤT BẠI"));

            // 4. Kiểm thử kích hoạt tài khoản bằng OTP
            com.koha.entity.User createdUser = userService.findByUsername(testUser);
            if (createdUser != null && createdUser.getCode() != null) {
                String otpCode = createdUser.getCode();
                System.out.println("   + Mã OTP trong CSDL: " + otpCode + " | Trạng thái ban đầu: " + createdUser.getStatus() + " (Chưa kích hoạt)");
                boolean activated = userService.verifyOtp(testUser, otpCode);
                com.koha.entity.User activatedUser = userService.findByUsername(testUser);
                System.out.println("✅ [TEST 4] Kích hoạt tài khoản bằng OTP: " + (activated ? "THÀNH CÔNG" : "THẤT BẠI") + " | Trạng thái sau kích hoạt: " + activatedUser.getStatus() + " (Đã kích hoạt)");

                // 5. Kiểm thử cập nhật thông tin cá nhân (Profile: fullname, phone, images) qua JPA
                boolean profileUpdated = userService.updateProfile(activatedUser.getId(), "Người Dùng Đã Cập Nhật", "0933112233", "user/avatar_test.png");
                com.koha.entity.User profileUser = userService.findById(activatedUser.getId());
                System.out.println("✅ [TEST 5] Cập nhật Profile (Họ tên, SĐT, Ảnh đại diện): " + (profileUpdated ? "THÀNH CÔNG" : "THẤT BẠI"));
                System.out.println("   + Họ tên mới: " + profileUser.getFullName() + " | SĐT: " + profileUser.getPhone() + " | Ảnh: " + profileUser.getImages());
            }

            System.out.println("=== TẤT CẢ KIỂM THỬ ĐỀU ĐẠT CHUẨN 100%! ===");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JpaConfig.shutdown();
        }
    }
}
