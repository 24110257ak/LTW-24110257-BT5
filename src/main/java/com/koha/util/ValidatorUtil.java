package com.koha.util;

import java.util.regex.Pattern;

/**
 * Lớp tiện ích kiểm tra dữ liệu đầu vào (Validation Utility)
 * Hỗ trợ kiểm tra định dạng email, số điện thoại Việt Nam, tên đăng nhập, mật khẩu, file hình ảnh, số thực, số nguyên.
 */
public class ValidatorUtil {

    // Regex kiểm tra định dạng email RFC 5322 chuẩn
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Regex kiểm tra số điện thoại Việt Nam: 10 chữ số, bắt đầu bằng 03, 05, 07, 08, 09
    private static final String PHONE_REGEX = "^(0[3|5|7|8|9])[0-9]{8}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    // Regex tên đăng nhập: từ 3 đến 50 ký tự, gồm chữ cái, chữ số và dấu gạch dưới
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,50}$";
    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);

    // Regex mã OTP: đúng 6 chữ số
    private static final String OTP_REGEX = "^[0-9]{6}$";
    private static final Pattern OTP_PATTERN = Pattern.compile(OTP_REGEX);

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidUsername(String username) {
        if (isEmpty(username)) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        return password.trim().length() >= 6;
    }

    public static boolean isValidOtp(String otp) {
        if (isEmpty(otp)) {
            return false;
        }
        return OTP_PATTERN.matcher(otp.trim()).matches();
    }

    public static boolean isValidLength(String str, int min, int max) {
        if (str == null) {
            return min <= 0;
        }
        int len = str.trim().length();
        return len >= min && len <= max;
    }

    public static boolean isValidImageUrl(String url) {
        if (isEmpty(url)) {
            return false;
        }
        String lower = url.trim().toLowerCase();
        return lower.startsWith("http://") || lower.startsWith("https://");
    }

    public static boolean isValidImageExtension(String filename) {
        if (isEmpty(filename)) {
            return false;
        }
        String lower = filename.trim().toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png") || lower.endsWith(".gif")
                || lower.endsWith(".webp");
    }

    public static Double parseDoubleSafe(String val) {
        if (isEmpty(val)) {
            return null;
        }
        try {
            return Double.parseDouble(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer parseIntSafe(String val) {
        if (isEmpty(val)) {
            return null;
        }
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
