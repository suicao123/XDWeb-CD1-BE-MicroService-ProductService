# 🔧 Fix Database Connection Error

## 🚨 Vấn đề
```
org.hibernate.exception.JDBCConnectionException: Unable to obtain isolated JDBC connection
Communications link failure - Connection refused
```

## ✅ Giải pháp

### 1️⃣ Xác nhận cấu hình `.env`

File `.env` của bạn phải chứa:
```env
SPRING_DATASOURCE_URL=jdbc:mysql://YOUR_DB_HOST:3306/shop_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### 2️⃣ Cập nhật các giá trị

**Thay đổi `YOUR_DB_HOST` bằng:**
- `localhost` - nếu database chạy trên máy host (Windows)
- `host.docker.internal` - nếu muốn container kết nối tới database trên Windows host
- `<IP-address>` - nếu database chạy trên server khác (ví dụ: `192.168.1.100`)
- `your-database-domain.com` - nếu database ở cloud

### 3️⃣ Chi tiết từng trường hợp

#### **Trường hợp A: Database đang chạy trên Windows (localhost)**
```env
SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/shop_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

#### **Trường hợp B: Database trên server khác**
```env
SPRING_DATASOURCE_URL=jdbc:mysql://192.168.1.100:3306/shop_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

#### **Trường hợp C: Database trên cloud (AWS RDS, etc)**
```env
SPRING_DATASOURCE_URL=jdbc:mysql://your-rds.amazonaws.com:3306/shop_db?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=secure_password
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

### 4️⃣ Restart Docker

Sau khi cập nhật `.env`, chạy:
```bash
# Dừng containers
docker-compose down

# Khởi động lại
docker-compose up -d

# Xem logs
docker-compose logs -f product-service
```

### 5️⃣ Kiểm tra kết nối

Khi thấy logs sau là thành công:
```
INFO [...] Tomcat initialized with port 8000
INFO [...] Root WebApplicationContext: initialization completed
```

Hoặc test API:
```bash
curl http://localhost:8000/api/products
```

## 🔍 Troubleshooting

### ❌ Vẫn Connection refused?

1. **Kiểm tra database đang chạy:**
   ```bash
   # Windows
   telnet localhost 3306
   ```

2. **Kiểm tra firewall:**
   - Đảm bảo port 3306 không bị chặn

3. **Kiểm tra .env file được load:**
   ```bash
   docker exec product-service-app env | grep SPRING_DATASOURCE
   ```

4. **Xem chi tiết logs:**
   ```bash
   docker-compose logs product-service | grep -i "datasource\|jdbc\|connection"
   ```

### ❌ Database URL vẫn [undefined/unknown]?

Đảm bảo:
1. `.env` file nằm cùng thư mục với `docker-compose.yml`
2. Không có lỗi syntax trong `.env`
3. Docker Compose có quyền đọc file
4. Khởi động lại với `docker-compose up -d --build`

## 📋 Checklist

- [ ] Cập nhật `.env` với database credentials đúng
- [ ] Kiểm tra database server đang chạy
- [ ] Kiểm tra network connectivity
- [ ] Chạy `docker-compose down` và `docker-compose up -d`
- [ ] Chờ 10-15 giây để Spring Boot khởi động
- [ ] Kiểm tra logs: `docker-compose logs product-service`
- [ ] Test API: `curl http://localhost:8000/api/products`

## 💡 Tips

- Mỗi lần thay đổi `.env` cần restart containers
- Sử dụng `host.docker.internal` cho database trên Windows host
- Trong production, sử dụng environment variables thay vì `.env` file
- Thêm `SPRING_JPA_HIBERNATE_DDL_AUTO=validate` trong production

