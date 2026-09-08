# 🚀 BÀI TẬP 5: SPRING BOOT 3 - CRUD ADMIN (CATEGORY & USER) VỚI JSP/JSTL

> **Môn học**: Lập trình Web  
> **Trường**: Đại học Sư phạm Kỹ thuật TP.HCM (HCMUTE)  
> **Mã số sinh viên (MSSV)**: 24110257  
> **Repository**: [https://github.com/24110257ak/LTW-24110257-BT5](https://github.com/24110257ak/LTW-24110257-BT5)  

---

## 📖 1. Giới thiệu Đề tài
Dự án được xây dựng và chuyển đổi hoàn chỉnh sang kiến trúc **Spring Boot 3.3.5** kết hợp **Spring Data JPA**, **Spring MVC**, **JSP/JSTL** và **SiteMesh 3 Decorator**. Hệ thống tập trung hoàn thiện các chức năng quản trị (Role Admin) cho bảng **Category** (Danh mục) và bảng **User** (Người dùng) kèm công cụ **tìm kiếm dữ liệu linh hoạt**.

---

## 🛠️ 2. Công nghệ & Thư viện sử dụng

* **Ngôn ngữ**: Java 17+ (Jakarta EE 10)
* **Framework**: Spring Boot 3.3.5
  * `spring-boot-starter-web`: Xây dựng Spring MVC REST & Controllers
  * `spring-boot-starter-data-jpa`: Tương tác CSDL thông qua Hibernate & Spring Data Repositories
  * `spring-boot-starter-validation`: Kiểm tra tính hợp lệ của dữ liệu đầu vào
* **View Engine**: JSP (JavaServer Pages) & JSTL (Jakarta Standard Tag Library 3.0)
  * `tomcat-embed-jasper`: Trình biên dịch JSP tích hợp trên nhúng Tomcat
* **Decorator & Layout**: SiteMesh 3.2.1
* **Cơ sở dữ liệu**: Microsoft SQL Server (kết nối qua `mssql-jdbc`)
* **Giao diện người dùng (UI)**: Bootstrap 5.3, FontAwesome 6 Icons

---

## ✨ 3. Các chức năng chính trong Role Admin

### 📂 A. Quản lý Danh mục (Category CRUD & Search)
* **Xem danh sách**: Hiển thị bảng danh mục gồm STT, ID, Hình ảnh biểu tượng, Tên danh mục, Trạng thái (Hoạt động / Tạm khóa), các nút thao tác.
* **Tìm kiếm theo tên**: Thanh tìm kiếm từ khóa không phân biệt chữ hoa/thường, hỗ trợ nút xóa bộ lọc.
* **Thêm mới danh mục**: Form nhập tên danh mục, chọn trạng thái, tải file ảnh từ máy tính hoặc dán URL ảnh trực tuyến.
* **Chỉnh sửa danh mục**: Cập nhật thông tin, hiển thị ảnh hiện tại và cho phép tải ảnh mới thay thế.
* **Xóa danh mục**: Có hộp thoại xác nhận xóa; xử lý an toàn ràng buộc khóa ngoại với sản phẩm.

### 👤 B. Quản lý Người dùng (User CRUD & Search)
* **Xem danh sách**: Hiển thị bảng tài khoản người dùng gồm Avatar, Tên đăng nhập, Họ và tên, Email, Số điện thoại, Vai trò (Badge Admin / User), Trạng thái (Badge Kích hoạt / Chưa kích hoạt).
* **Tìm kiếm đa trường**: Tìm kiếm nhanh theo **Username**, **Họ tên**, **Email** hoặc **Số điện thoại**.
* **Thêm mới người dùng**:
  * Kiểm tra hợp lệ 2 tầng (Client-side & Server-side): bắt buộc nhập tên đăng nhập (3-30 ký tự, không trùng lặp), mật khẩu (ít nhất 3 ký tự), họ tên, kiểm tra định dạng email và số điện thoại.
  * Phân quyền trực quan (Admin / User) và thiết lập trạng thái tài khoản.
  * Hỗ trợ upload ảnh đại diện (kèm xem trước ảnh trực tiếp bằng JavaScript).
* **Chỉnh sửa người dùng**: Cập nhật thông tin tài khoản, phân quyền, trạng thái; cho phép đổi mật khẩu mới hoặc để trống để giữ nguyên mật khẩu cũ.
* **Xóa người dùng**: Tích hợp cơ chế bảo vệ ngăn chặn Admin tự xóa tài khoản đang đăng nhập.

### 🖼️ C. Xử lý Hình ảnh & Static Uploads
* Streaming ảnh vật lý an toàn từ thư mục `uploads/` thông qua `ImageController` (`/image?fname=...`).
* Tự động sinh ảnh SVG placeholder mặc định nếu ảnh không tồn tại.

---

## 🗄️ 4. Cấu trúc Cơ sở Dữ liệu (SQL Server)

* Database: `bt27082026`
* File script: [`database.sql`](./database.sql)
* Các bảng chính:
  1. `users`: Quản lý tài khoản, mật khẩu, họ tên, email, phone, roleid (1: Admin, 2: User), status (1: Kích hoạt, 0: Chưa kích hoạt), avatar `images`.
  2. `categories`: Quản lý danh mục `CategoryId`, `CategoryName`, `Images`, `Status`.
  3. `products`: Quản lý sản phẩm, liên kết khóa ngoại với `categories`.

---

## 📂 5. Cấu trúc Thư mục Dự án

```
bt5(8-9-2026 - 10-9-2026)/
├── pom.xml                                    # Cấu hình Maven & Spring Boot 3
├── database.sql                               # Script tạo CSDL & dữ liệu mẫu
├── README.md                                  # Tài liệu hướng dẫn dự án
├── src/main/resources/
│   └── application.properties                 # Cấu hình kết nối SQL Server, View Resolver JSP
├── src/main/java/com/koha/
│   ├── Application.java                       # Class khởi chạy Spring Boot chính (@SpringBootApplication)
│   ├── config/
│   │   └── WebMvcConfig.java                  # Đăng ký SiteMeshFilter & Static Resource Handlers
│   ├── entity/                                # Các đối tượng JPA Entity (Category, User, Product)
│   ├── repository/                            # Tầng Spring Data JPA (CategoryRepository, UserRepository,...)
│   ├── service/                               # Tầng Service xử lý nghiệp vụ (ICategoryService, IUserService,...)
│   ├── controller/                            # Tầng Controller (Spring MVC)
│   │   ├── CategoryController.java            # Quản lý CRUD Category (/admin/categories,...)
│   │   ├── AdminUserController.java           # Quản lý CRUD User (/admin/users,...)
│   │   ├── ImageController.java               # Trả về stream ảnh (/image?fname=...)
│   │   └── HomeController.java                # Trang chủ & điều hướng khách
│   ├── filter/
│   │   └── MySiteMeshFilter.java              # Tùy biến bộ lọc giao diện SiteMesh
│   └── util/
│       ├── Constant.java                      # Các hằng số thư mục upload, email
│       └── ValidatorUtil.java                 # Tiện ích kiểm tra hợp lệ dữ liệu
└── src/main/webapp/
    ├── WEB-INF/
    │   ├── decorators/
    │   │   ├── admin.jsp                      # Layout trang Quản trị Admin
    │   │   └── web.jsp                        # Layout trang Khách hàng
    │   └── sitemesh3.xml                      # Cấu hình mapping Decorator
    └── views/
        ├── admin/
        │   ├── category-list.jsp              # Bảng danh sách & tìm kiếm Category
        │   ├── category-add.jsp               # Form thêm mới Category
        │   ├── category-edit.jsp              # Form chỉnh sửa Category
        │   ├── user-list.jsp                  # Bảng danh sách & tìm kiếm User
        │   ├── user-add.jsp                   # Form thêm mới User + Preview avatar
        │   └── user-edit.jsp                  # Form chỉnh sửa User
        ├── index.jsp                          # Trang chủ hệ thống
        └── login.jsp                          # Trang đăng nhập
```

---

## 🚀 6. Hướng dẫn Cài đặt & Chạy Dự án

### Bước 1: Khởi tạo Cơ sở Dữ liệu
1. Mở **SQL Server Management Studio (SSMS)**.
2. Mở và thực thi file script [`database.sql`](./database.sql).
3. Đảm bảo tài khoản `sa` với mật khẩu `123` có quyền truy cập vào CSDL `bt27082026` (hoặc cập nhật thông tin trong file `application.properties`).

### Bước 2: Chạy ứng dụng bằng Maven / IDE
* **Chạy bằng dòng lệnh:**
  ```powershell
  mvn spring-boot:run
  ```
* **Hoặc chạy trong IDE (VS Code / Eclipse / IntelliJ):**
  * Mở class `com.koha.Application.java`.
  * Nhấp chuột phải chọn **Run Java** / **Run As Spring Boot App**.

### Bước 3: Truy cập hệ thống trên Trình duyệt
* **Trang chủ**: [http://localhost:8080/home](http://localhost:8080/home)
* **Đăng nhập**: [http://localhost:8080/login](http://localhost:8080/login)
* **Quản lý Danh mục (Admin)**: [http://localhost:8080/admin/categories](http://localhost:8080/admin/categories)
* **Quản lý Người dùng (Admin)**: [http://localhost:8080/admin/users](http://localhost:8080/admin/users)
* **Quản lý Sản phẩm (Admin)**: [http://localhost:8080/admin/products](http://localhost:8080/admin/products)

### 🔑 Tài khoản thử nghiệm có sẵn:
| Vai trò | Tên đăng nhập | Mật khẩu | Ghi chú |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin` | `123` | Toàn quyền quản trị hệ thống |
| **Admin** | `trungnh` | `123` | Giảng viên ThS. Nguyễn Hữu Trung |
| **User** | `user1` | `123456` | Tài khoản người dùng mẫu |
