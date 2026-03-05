CREATE DATABASE IF NOT EXISTS shop_db;
USE shop_db;

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    productname VARCHAR(255) NOT NULL,
    price DECIMAL(15, 2) NOT NULL,
    image VARCHAR(255),
    description TEXT NOT NULL, -- Bắt buộc theo đặc tả
    status TINYINT(1) DEFAULT 1, -- 1: In Stock, 0: Out of Stock
    is_new TINYINT(1) DEFAULT 0, -- Dùng cho tham số ?new=true
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL, -- Giả định đã có bảng users
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

INSERT INTO products (id, productname, price, image, description, status, is_new) VALUES
(1, 'iPhone 15 Pro Max', 30000000, '/media/iphone.jpg', 'Chip A17 Pro, khung Titan, camera 48MP...', 1, 1),
(2, 'Samsung S24 Ultra', 25000000, 'https://link-anh-online.com/samsung.jpg', 'Snapdragon 8 Gen 3, Camera 200MP, Bút S-Pen', 1, 0),
(101, 'iPhone 15 Pro Max Titanium', 34990000, '/media/iphone15.png', 'Phiên bản titan đặc biệt, dung lượng cao', 1, 1),
(102, 'Samsung Galaxy S24 Ultra', 29990000, '/images/samsung-s24.jpg', 'Màn hình phẳng, khung viền titan mới', 1, 0),
(109, 'Ốp lưng iPhone 15', 500000, '/media/case-iphone.jpg', 'Ốp lưng silicon chính hãng Apple', 1, 0),
(110, 'MacBook Air M3', 28000000, '/media/macbook.jpg', 'Chip M3 siêu mạnh mẽ, mỏng nhẹ', 0, 1); -- Trạng thái hết hàng (status: 0)