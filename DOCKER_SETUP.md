# Docker Setup Guide - Product Service

## 📋 Yêu cầu
- Docker: version 20.10+
- Docker Compose: version 2.0+
- MySQL Database (deployed separately)
- Git

## 🚀 Hướng dẫn Deploy

### 1. Chuẩn bị Database
Đảm bảo bạn có một MySQL server được deploy và chạy, với:
- Host: `<your-database-host>`
- Port: `3306`
- Database: `shop_db`
- Username: `<your-database-user>`
- Password: `<your-database-password>`

### 2. Clone hoặc chuẩn bị dự án
```bash
cd C:\Users\MINH\IdeaProjects\XDWeb-CD1-BE-MicroService-ProductService
```

### 3. Build và chạy với Docker Compose

#### Option A: Dùng environment variables
```bash
# Export variables
export SPRING_DATASOURCE_URL=jdbc:mysql://<your-host>:3306/shop_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
export SPRING_DATASOURCE_USERNAME=<your-username>
export SPRING_DATASOURCE_PASSWORD=<your-password>

# Run
docker-compose up -d
```

#### Option B: Tạo file .env (khuyến nghị)
Tạo file `.env` trong project root:
```env
SPRING_DATASOURCE_URL=jdbc:mysql://<your-host>:3306/shop_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=<your-username>
SPRING_DATASOURCE_PASSWORD=<your-password>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

Sau đó:
```bash
docker-compose up -d
```

#### Option C: Chỉnh sửa docker-compose.yml trực tiếp
Cập nhật environment variables trong `docker-compose.yml`:
```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:mysql://<your-host>:3306/shop_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
  SPRING_DATASOURCE_USERNAME: <your-username>
  SPRING_DATASOURCE_PASSWORD: <your-password>
```

### 4. Kiểm tra trạng thái
```bash
# Xem logs của ứng dụng
docker-compose logs -f product-service

# Kiểm tra container đang chạy
docker-compose ps
```

### 5. Truy cập ứng dụng
- **Product Service API**: http://localhost:8000

## 🛑 Dừng ứng dụng
```bash
# Dừng tất cả containers
docker-compose down
```

## 🔍 Kiểm tra chi tiết

### Xem logs real-time
```bash
docker-compose logs -f
```

### Truy cập Product Service container
```bash
docker exec -it product-service-app /bin/bash
```

### Test database connection
```bash
docker exec -it product-service-app curl http://localhost:8000/actuator/health
```

## 📊 File Structure
```
project-root/
├── Dockerfile              # Build image cho Product Service
├── docker-compose.yml      # Cấu hình Docker Compose (chỉ app service)
├── .dockerignore          # Files bỏ qua khi build
├── .env                   # Environment variables (không commit)
├── pom.xml               # Maven dependencies
├── Schema.sql            # Database schema
├── src/                  # Source code
└── DOCKER_SETUP.md       # Hướng dẫn này
```

## 🔧 Environment Variables

### Product Service
- `SPRING_DATASOURCE_URL`: JDBC URL cho MySQL
  - Default: `jdbc:mysql://localhost:3306/shop_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`
- `SPRING_DATASOURCE_USERNAME`: Database username
  - Default: `root`
- `SPRING_DATASOURCE_PASSWORD`: Database password
  - Default: `root`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Hibernate DDL strategy
  - Default: `update`
  - Options: `create`, `create-drop`, `update`, `validate`, `none`
- `SPRING_APPLICATION_NAME`: Application name
  - Default: `product-service`

## ⚠️ Troubleshooting

### 1. Cannot connect to database
```
Error: java.sql.SQLException: Cannot get a connection
```

**Solutions:**
- Kiểm tra `SPRING_DATASOURCE_URL` có chính xác không
- Kiểm tra username/password
- Đảm bảo MySQL server đang chạy
- Kiểm tra firewall/network connectivity
- Chạy từ container để kiểm tra: 
  ```bash
  docker exec -it product-service-app curl <your-database-host>:3306
  ```

### 2. Port 8000 đã được sử dụng
```bash
# Windows: Tìm process sử dụng port
netstat -ano | findstr :8000

# Kill process
taskkill /PID <PID> /F

# Hoặc change port trong docker-compose.yml
ports:
  - "8001:8000"  # Map port 8001 -> 8000
```

### 3. Application not responding
```bash
# Xem logs
docker-compose logs product-service

# Kiểm tra health
curl http://localhost:8000/actuator/health
```

## 🔒 Security Notes
- **Không commit `.env` file** vào repository
- Sử dụng secure passwords trong production
- Hạn chế network access nếu có thể
- Sử dụng SSL/TLS khi kết nối database
- Thay đổi default credentials

## 📝 Production Deployment

1. **Prepare .env file:**
   ```env
   SPRING_DATASOURCE_URL=jdbc:mysql://prod-db-host:3306/shop_db?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=false
   SPRING_DATASOURCE_USERNAME=prod_user
   SPRING_DATASOURCE_PASSWORD=secure_password_here
   SPRING_JPA_HIBERNATE_DDL_AUTO=validate
   ```

2. **Update docker-compose.yml:**
   ```yaml
   environment:
     SPRING_JPA_HIBERNATE_DDL_AUTO: validate  # Không tự động update schema
   restart: unless-stopped
   ```

3. **Run:**
   ```bash
   docker-compose -f docker-compose.yml up -d
   ```

4. **Monitor:**
   ```bash
   docker-compose logs -f
   ```

## 🐳 Docker Commands Reference

```bash
# Build image
docker-compose build

# Run containers
docker-compose up -d

# Rebuild và run
docker-compose up -d --build

# Stop containers
docker-compose stop

# Remove containers
docker-compose rm

# View logs
docker-compose logs -f <service-name>

# Execute command in container
docker exec -it product-service-app <command>

# Remove everything
docker-compose down

# Push to registry
docker tag product-service:latest <registry>/<image>:<tag>
docker push <registry>/<image>:<tag>
```

## 📋 Quick Reference Commands

```bash
# Start
docker-compose up -d

# Stop
docker-compose down

# View logs
docker-compose logs -f

# Health check
docker-compose ps

# View specific logs
docker-compose logs product-service

# Enter container shell
docker exec -it product-service-app bash

# Rebuild
docker-compose up -d --build
```

