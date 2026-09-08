package com.koha.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.koha.util.Constant;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ImageController {

    @GetMapping("/image")
    public void getImage(@RequestParam(value = "fname", required = false) String fileName, HttpServletResponse response)
            throws IOException {
        if (fileName != null && !fileName.trim().isEmpty()) {
            File file = new File(Constant.DIR + "/" + fileName);
            if (!file.exists()) {
                file = new File(Constant.USER_UPLOAD_DIR + "/" + fileName);
            }
            if (!file.exists()) {
                file = new File(Constant.CATEGORY_UPLOAD_DIR + "/" + fileName);
            }
            if (!file.exists()) {
                file = new File(Constant.PRODUCT_UPLOAD_DIR + "/" + fileName);
            }

            if (file.exists() && file.isFile()) {
                String name = file.getName().toLowerCase();
                if (name.endsWith(".png")) {
                    response.setContentType("image/png");
                } else if (name.endsWith(".gif")) {
                    response.setContentType("image/gif");
                } else if (name.endsWith(".webp")) {
                    response.setContentType("image/webp");
                } else {
                    response.setContentType("image/jpeg");
                }
                response.setContentLengthLong(file.length());

                try (FileInputStream in = new FileInputStream(file);
                     OutputStream out = response.getOutputStream()) {
                    in.transferTo(out);
                    out.flush();
                }
                return;
            }
        }

        // SVG placeholder mặc định nếu file không tồn tại
        response.setContentType("image/svg+xml;charset=UTF-8");
        String defaultSvg = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"120\" height=\"120\" viewBox=\"0 0 120 120\">"
                + "<rect width=\"120\" height=\"120\" fill=\"#e9ecef\" rx=\"8\"/>"
                + "<path d=\"M35 80 L55 55 L70 72 L85 50 L95 80 Z\" fill=\"#adb5bd\"/>"
                + "<circle cx=\"45\" cy=\"42\" r=\"7\" fill=\"#adb5bd\"/>"
                + "<text x=\"60\" y=\"105\" font-family=\"Arial\" font-size=\"11\" fill=\"#6c757d\" text-anchor=\"middle\">No Image</text>"
                + "</svg>";
        response.getWriter().write(defaultSvg);
    }
}
