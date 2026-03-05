package com.example.controller;

import com.example.dto.CartItemDTO;
import com.example.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

