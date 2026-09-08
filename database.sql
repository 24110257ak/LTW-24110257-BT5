-- Script Database cho Bài tập bt27082026 (JPA 3.0 & Servlet 6.0)
-- 1. Tạo Database bt27082026 nếu chưa tồn tại
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'bt27082026')
BEGIN
    CREATE DATABASE bt27082026;
END
GO

USE bt27082026;
GO

-- 2. Tạo bảng users (Login Cookie/Session, Kích hoạt OTP, Quên mật khẩu OTP)
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'users')
BEGIN
    CREATE TABLE users (
        id INT IDENTITY(1,1) PRIMARY KEY,
        username NVARCHAR(50) NOT NULL UNIQUE,
        password NVARCHAR(255) NOT NULL,
        fullname NVARCHAR(100) NOT NULL,
        email NVARCHAR(150) NULL,
        phone NVARCHAR(20) NULL,
        roleid INT DEFAULT 2,          -- 1: Admin, 2: User
        status INT DEFAULT 0,          -- 0: Chưa kích hoạt, 1: Đã kích hoạt
        code NVARCHAR(10) NULL,        -- Lưu mã OTP kích hoạt / quên mật khẩu
        images NVARCHAR(500) NULL      -- Ảnh đại diện người dùng
    );

    INSERT INTO users (username, password, fullname, email, phone, roleid, status, code, images) VALUES 
    (N'admin', N'123', N'Quản Trị Viên', N'admin@iotstar.vn', N'0908888999', 1, 1, NULL, N'https://cdn-icons-png.flaticon.com/512/3135/3135715.png'),
    (N'user1', N'123456', N'Nguyễn Văn A', N'vana@gmail.com', N'0912345678', 2, 1, NULL, N'https://cdn-icons-png.flaticon.com/512/3135/3135715.png'),
    (N'trungnh', N'123', N'ThS. Nguyễn Hữu Trung', N'trungnh@hcmute.edu.vn', N'0908617108', 1, 1, NULL, N'https://cdn-icons-png.flaticon.com/512/3135/3135715.png');
END
ELSE
BEGIN
    -- Đảm bảo có các cột status, code, images nếu bảng users đã tồn tại trước đó
    IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'status')
    BEGIN
        ALTER TABLE users ADD status INT DEFAULT 1;
        EXEC('UPDATE users SET status = 1 WHERE status IS NULL');
    END
    IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'code')
    BEGIN
        ALTER TABLE users ADD code NVARCHAR(10) NULL;
    END
    IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'images')
    BEGIN
        ALTER TABLE users ADD images NVARCHAR(500) NULL;
        EXEC('UPDATE users SET images = ''https://cdn-icons-png.flaticon.com/512/3135/3135715.png'' WHERE images IS NULL');
    END
END
GO

-- 3. Tạo bảng categories (CRUD Category JPA)
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'categories')
BEGIN
    CREATE TABLE categories (
        CategoryId INT IDENTITY(1,1) PRIMARY KEY,
        CategoryName NVARCHAR(255) NOT NULL,
        Images NVARCHAR(500) NULL,
        Status INT DEFAULT 1
    );

    INSERT INTO categories (CategoryName, Images, Status) VALUES 
    (N'Điện Thoại & Di Động', N'https://cdn.tgdd.vn/Products/Images/42/305658/iphone-15-pro-max-blue-thumbnew-600x600.jpg', 1),
    (N'Laptop & Máy Tính Bảng', N'https://cdn.tgdd.vn/Products/Images/44/313333/macbook-air-13-m3-gray-thumb-600x600.jpg', 1),
    (N'Phụ Kiện Công Nghệ', N'https://cdn.tgdd.vn/Products/Images/54/236016/tai-nghe-bluetooth-airpods-2-apple-thumb-600x600.jpg', 1),
    (N'Thiết Bị Gia Dụng', N'https://cdn.tgdd.vn/Products/Images/1982/222213/robot-hut-bui-xiaomi-vacuum-mop-2-pro-thumb-600x600.jpg', 1);
END
GO

-- 4. Tạo bảng products (CRUD Products, Quan hệ 1 - n với Category, Top 10 SP mới, Phân trang 6sp/trang)
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'products')
BEGIN
    CREATE TABLE products (
        ProductId INT IDENTITY(1,1) PRIMARY KEY,
        ProductName NVARCHAR(255) NOT NULL,
        Description NVARCHAR(MAX) NULL,
        Price DECIMAL(18,2) NOT NULL DEFAULT 0,
        Images NVARCHAR(500) NULL,
        Quantity INT NOT NULL DEFAULT 0,
        Status INT NOT NULL DEFAULT 1,     -- 1: Đang bán, 0: Tạm ngừng
        CategoryId INT NOT NULL,
        CreatedDate DATETIME DEFAULT GETDATE(),
        CONSTRAINT FK_Products_Categories FOREIGN KEY (CategoryId) 
            REFERENCES categories(CategoryId) ON DELETE CASCADE
    );

    -- Chèn 14 sản phẩm mẫu phục vụ test phân trang 6sp/trang và hiển thị 10 sản phẩm mới nhất
    INSERT INTO products (ProductName, Description, Price, Images, Quantity, Status, CategoryId, CreatedDate) VALUES
    (N'iPhone 15 Pro Max 256GB', N'Titan tự nhiên sang trọng, chip Apple A17 Pro mạnh mẽ, camera tiềm vọng 5x đỉnh cao.', 29490000, N'https://cdn.tgdd.vn/Products/Images/42/305658/iphone-15-pro-max-blue-thumbnew-600x600.jpg', 50, 1, 1, DATEADD(minute, 1, GETDATE())),
    (N'Samsung Galaxy S24 Ultra 512GB', N'Quyền năng Galaxy AI, camera zoom 100x, bút S-Pen thông minh và màn hình Dynamic AMOLED 2X.', 31990000, N'https://cdn.tgdd.vn/Products/Images/42/307174/samsung-galaxy-s24-ultra-grey-thumbnew-600x600.jpg', 40, 1, 1, DATEADD(minute, 2, GETDATE())),
    (N'Xiaomi 14 Ultra 5G Leica', N'Hệ thống 4 ống kính quang học Leica đỉnh cao, chip Snapdragon 8 Gen 3 cực khủng.', 26990000, N'https://cdn.tgdd.vn/Products/Images/42/313886/xiaomi-14-ultra-den-thumb-600x600.jpg', 30, 1, 1, DATEADD(minute, 3, GETDATE())),
    (N'OPPO Find N3 Flip 5G', N'Thiết kế gập vỏ sò thời thượng, màn hình ngoài lớn đa năng, camera Hasselblad chụp chân dung chuyên nghiệp.', 18990000, N'https://cdn.tgdd.vn/Products/Images/42/313883/oppo-find-n3-flip-vang-thumb-600x600.jpg', 25, 1, 1, DATEADD(minute, 4, GETDATE())),
    (N'MacBook Air 13 inch M3 2024', N'Mỏng nhẹ ấn tượng, vi xử lý M3 vượt trội, thời lượng pin cả ngày lên đến 18 giờ liên tục.', 27490000, N'https://cdn.tgdd.vn/Products/Images/44/313333/macbook-air-13-m3-gray-thumb-600x600.jpg', 35, 1, 2, DATEADD(minute, 5, GETDATE())),
    (N'Laptop ASUS ROG Zephyrus G16', N'Laptop gaming cao cấp màn hình OLED 240Hz, card đồ họa RTX 4070, thiết kế kim loại siêu mỏng.', 52990000, N'https://cdn.tgdd.vn/Products/Images/44/322894/asus-rog-zephyrus-g16-gu605mi-ultra-9-u908w-thumb-600x600.jpg', 15, 1, 2, DATEADD(minute, 6, GETDATE())),
    (N'iPad Pro 11 inch M4 Wi-Fi 256GB', N'Màn hình Ultra Retina XDR OLED kép siêu mỏng 5.3mm, chip M4 đỉnh cao hiệu năng đồ họa.', 26990000, N'https://cdn.tgdd.vn/Products/Images/522/325492/ipad-pro-11-inch-m4-wifi-den-thumb-600x600.jpg', 45, 1, 2, DATEADD(minute, 7, GETDATE())),
    (N'Laptop Dell XPS 13 Plus 9320', N'Thiết kế tương lai với hàng phím chức năng cảm ứng, touchpad vô cực ẩn dưới kính cường lực.', 45990000, N'https://cdn.tgdd.vn/Products/Images/44/287311/dell-xps-13-plus-9320-i7-5cg29-thumb-600x600.jpg', 20, 1, 2, DATEADD(minute, 8, GETDATE())),
    (N'Tai nghe Apple AirPods Pro 2 MagSafe', N'Chống ồn chủ động ANC thế hệ 2 gấp 2 lần, âm thanh thích ứng và cổng sạc Type-C tiện lợi.', 5390000, N'https://cdn.tgdd.vn/Products/Images/54/236016/tai-nghe-bluetooth-airpods-2-apple-thumb-600x600.jpg', 80, 1, 3, DATEADD(minute, 9, GETDATE())),
    (N'Tai nghe chụp tai Sony WH-1000XM5', N'Chống ồn hàng đầu thị trường, 8 micro thu âm, chất âm Hi-Res Audio tinh tế đỉnh cao.', 7490000, N'https://cdn.tgdd.vn/Products/Images/54/280540/tai-nghe-chup-tai-sony-wh-1000xm5-den-thumb-600x600.jpg', 60, 1, 3, DATEADD(minute, 10, GETDATE())),
    (N'Đồng hồ thông minh Apple Watch Ultra 2', N'Khung titan chuẩn quân đội 49mm, định vị GPS kép chính xác cao, pin lên đến 72 giờ.', 20990000, N'https://cdn.tgdd.vn/Products/Images/7077/315367/apple-watch-ultra-2-gps-cellular-49mm-vien-titanium-day-ocean-thumb-600x600.jpg', 30, 1, 3, DATEADD(minute, 11, GETDATE())),
    (N'Sạc dự phòng Anker 737 PowerCore 24K', N'Công suất khủng 140W chuẩn PD 3.1 sạc nhanh cho MacBook Pro, màn hình OLED hiển thị thông số.', 2490000, N'https://cdn.tgdd.vn/Products/Images/58/293297/pin-sac-du-phong-polymer-24000mah-140w-anker-737-a1289-thumb-600x600.jpg', 90, 1, 3, DATEADD(minute, 12, GETDATE())),
    (N'Robot hút bụi lau nhà Xiaomi X20+', N'Trạm giặt giẻ sấy khô tự động, lực hút siêu mạnh 6000Pa, tránh vật cản chính xác bằng laser.', 11990000, N'https://cdn.tgdd.vn/Products/Images/1982/222213/robot-hut-bui-xiaomi-vacuum-mop-2-pro-thumb-600x600.jpg', 25, 1, 4, DATEADD(minute, 13, GETDATE())),
    (N'Nồi chiên không dầu Philips XXL HD9650', N'Công nghệ Twin TurboStar loại bỏ đến 90% lượng dầu mỡ thừa, dung tích lớn cho cả gia đình.', 4890000, N'https://cdn.tgdd.vn/Products/Images/1982/236021/noi-chien-khong-dau-philips-hd9650-thumb-600x600.jpg', 40, 1, 4, DATEADD(minute, 14, GETDATE()));
END
GO
