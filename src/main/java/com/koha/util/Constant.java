package com.koha.util;

import java.io.File;

public class Constant {

    // Thư mục lưu trữ upload nằm bên trong workspace bt2 + 3 + 4(27-8-2026-6-9-2026)
    public static final String DIR = System.getProperty("upload.dir", "d:/lap_trinh_web/bt2 + 3 + 4(27-8-2026-6-9-2026)/uploads");
    // Thư mục lưu trữ upload nằm bên trong workspace bt5(8-9-2026 - 10-9-2026)
    public static final String DIR = System.getProperty("upload.dir", "d:/lap_trinh_web/bt5(8-9-2026 - 10-9-2026)/uploads");
    public static final String CATEGORY_UPLOAD_DIR = DIR + "/category";
    public static final String PRODUCT_UPLOAD_DIR = DIR + "/product";
    public static final String USER_UPLOAD_DIR = DIR + "/user";
    public static final String DEFAULT_FILENAME = "default.file";

    public static final String COOKIE_REMEMBER = "username";
    public static final String SESSION_USERNAME = "user";

    // Cấu hình Email gửi OTP qua Gmail SMTP (Sử dụng App Password do bạn cung cấp)
    public static final String SENDER_EMAIL = System.getProperty("app.sender.email", "24110257@student.hcmute.edu.vn");
    public static final String SENDER_PASSWORD = System.getProperty("app.sender.password", "walsignkzfiuibgw");

    static {
        File dir = new File(CATEGORY_UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File productDir = new File(PRODUCT_UPLOAD_DIR);
        if (!productDir.exists()) {
            productDir.mkdirs();
        }
        File userDir = new File(USER_UPLOAD_DIR);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
    }
}
