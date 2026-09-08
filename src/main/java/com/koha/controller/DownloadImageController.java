package com.koha.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.koha.util.Constant;

@WebServlet(urlPatterns = {"/image"})
// @WebServlet(urlPatterns = {"/image"}) - Đã được thay thế bởi Spring MVC ImageController
public class DownloadImageController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getParameter("fname");
        if (fileName != null && !fileName.trim().isEmpty()) {
            File file = new File(Constant.DIR + "/" + fileName);
            if (file.exists() && file.isFile()) {
                String mimeType = req.getServletContext().getMimeType(file.getName());
                if (mimeType == null) {
                    if (fileName.toLowerCase().endsWith(".png")) {
                        mimeType = "image/png";
                    } else if (fileName.toLowerCase().endsWith(".gif")) {
                        mimeType = "image/gif";
                    } else if (fileName.toLowerCase().endsWith(".webp")) {
                        mimeType = "image/webp";
                    } else {
                        mimeType = "image/jpeg";
                    }
                }
                resp.setContentType(mimeType);
                resp.setContentLengthLong(file.length());

                try (FileInputStream in = new FileInputStream(file);
                     OutputStream out = resp.getOutputStream()) {
                    in.transferTo(out);
                    out.flush();
                }
                return;
            }
        }

        // Ảnh mặc định SVG placeholder nếu file không tồn tại
        resp.setContentType("image/svg+xml;charset=UTF-8");
        String defaultSvg = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"120\" height=\"120\" viewBox=\"0 0 120 120\">"
                + "<rect width=\"120\" height=\"120\" fill=\"#e9ecef\" rx=\"8\"/>"
                + "<path d=\"M35 80 L55 55 L70 72 L85 50 L95 80 Z\" fill=\"#adb5bd\"/>"
                + "<circle cx=\"45\" cy=\"42\" r=\"7\" fill=\"#adb5bd\"/>"
                + "<text x=\"60\" y=\"105\" font-family=\"Arial\" font-size=\"11\" fill=\"#6c757d\" text-anchor=\"middle\">No Image</text>"
                + "</svg>";
        resp.getWriter().write(defaultSvg);
    }
}

