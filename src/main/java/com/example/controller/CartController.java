package com.example.controller;

import com.example.dto.AddToCartDTO;
import com.example.dto.CartItemDTO;
import com.example.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
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

    /**
     * Lấy danh sách giỏ hàng của user hiện tại.
     * userId được trích xuất từ JWT token (đã được JwtAuthFilter xử lý).
     */
    @GetMapping
    public ResponseEntity<?> getCart(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        List<CartItemDTO> cartItems = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    /**
     * Thêm sản phẩm vào giỏ hàng.
     * userId được trích xuất từ JWT token.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartDTO request,
            HttpServletRequest httpRequest) {

        Integer userId = (Integer) httpRequest.getAttribute("userId");

        try {
            cartService.addToCart(userId, request.getProduct_id(), request.getQuantity());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Đã thêm vào giỏ hàng thành công!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Xóa một item khỏi giỏ hàng theo id.
     * userId từ token dùng để xác minh item thuộc về user này.
     */
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeCartItem(
            @PathVariable Integer id,
            HttpServletRequest request) {

        Integer userId = (Integer) request.getAttribute("userId");
        try {
            cartService.removeCartItem(id, userId);
            return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}


