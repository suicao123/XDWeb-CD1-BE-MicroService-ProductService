package com.example.controller;

import com.example.dto.AddToCartDTO;
import com.example.dto.CartItemDTO;
import com.example.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<?> getCart(@RequestHeader(value = "Authorization", required = false) String authHeader) {

//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Thiếu hoặc sai token xác thực.");
//        }
//
//        String token = authHeader.substring(7);
//
        Integer userId = 1;

        List<CartItemDTO> cartItems = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartDTO request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // Kiểm tra Authorization header
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("error", "Thiếu hoặc sai token xác thực."));
//        }
//
//        // Lấy token (có thể dùng để xác thực từ user service)
//        String token = authHeader.substring(7);

        // Tạm thời sử dụng userId = 1
        Integer userId = 1;

        try {
            cartService.addToCart(userId, request.getProduct_id(), request.getQuantity());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Đã thêm vào giỏ hàng thành công!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeCartItem(
            @PathVariable Integer id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        cartService.removeCartItem(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}

