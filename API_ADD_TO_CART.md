# API Thêm Vào Giỏ Hàng (Add to Cart)

## Endpoint Details

**URL:** `http://localhost:8000/api/cart/add/`  
**Method:** `POST`  
**Authentication:** BẮT BUỘC (Bearer Token)

---

## Request Header
```
Authorization: Bearer <token>
Content-Type: application/json
```

## Request Body
```json
{
  "product_id": 101,
  "quantity": 2
}
```

### Field Description
- `product_id` (Integer): ID của sản phẩm cần thêm vào giỏ
- `quantity` (Integer): Số lượng sản phẩm muốn thêm

---

## Success Response (HTTP 201)
```json
{
  "message": "Đã thêm vào giỏ hàng thành công!"
}
```

---

## Error Responses

### 1. Missing/Invalid Authorization Header (HTTP 401)
```json
{
  "error": "Thiếu hoặc sai token xác thực."
}
```

### 2. Product Not Found (HTTP 400)
```json
{
  "error": "Sản phẩm không tồn tại"
}
```

---

## Implementation Notes

### Các file được tạo/sửa:

1. **AddToCartDTO.java** (Mới)
   - DTO để nhận dữ liệu từ request
   - Fields: `product_id`, `quantity`

2. **CartController.java** (Cập nhật)
   - Thêm endpoint `@PostMapping("/add/")`
   - Kiểm tra Bearer token trong Authorization header
   - Gọi service để xử lý logic

3. **CartService.java** (Cập nhật)
   - Thêm method `addToCart(userId, productId, quantity)`
   - Logic: Nếu sản phẩm đã có trong giỏ → cập nhật số lượng
   - Logic: Nếu sản phẩm chưa có → tạo mới CartItem
   - Kiểm tra sản phẩm tồn tại qua ProductRepository

4. **CartItemRepository.java** (Cập nhật)
   - Thêm method `findByUserIdAndProductId()` để check sản phẩm có trong giỏ

---

## How It Works

1. Frontend gửi POST request tới `/api/cart/add/` với Bearer token
2. Backend kiểm tra Authorization header
3. Nếu không có token → trả về lỗi 401
4. Nếu có token → lấy userId từ token (hiện tại hard-coded là 1)
5. Backend kiểm tra sản phẩm có tồn tại
6. Nếu sản phẩm chưa có trong giỏ → tạo CartItem mới
7. Nếu sản phẩm đã có trong giỏ → cập nhật quantity bằng cách cộng thêm
8. Trả về response 201 với message thành công

---

## Example Usage with cURL

```bash
curl -X POST http://localhost:8000/api/cart/add/ \
  -H "Authorization: Bearer eyJhbGc..." \
  -H "Content-Type: application/json" \
  -d '{
    "product_id": 101,
    "quantity": 2
  }'
```

## Example Usage with Postman

1. Method: POST
2. URL: http://localhost:8000/api/cart/add/
3. Headers:
   - Authorization: Bearer <token>
   - Content-Type: application/json
4. Body (raw, JSON):
```json
{
  "product_id": 101,
  "quantity": 2
}
```
5. Click Send

---

## Notes

- Hiện tại `userId` được hard-code là 1. Trong thực tế, cần parse từ JWT token
- Nếu thêm cùng sản phẩm lần 2 với quantity 2, tổng sẽ là 4
- Sản phẩm phải tồn tại trong database, nếu không sẽ trả về lỗi

