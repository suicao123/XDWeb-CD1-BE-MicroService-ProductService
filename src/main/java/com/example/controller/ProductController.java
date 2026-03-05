package com.example.controller;

import com.example.dto.ProductDTO;
import com.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts(
            @RequestParam(value = "new", required = false) Boolean isNew) {

        List<ProductDTO> products;

        if (isNew != null && isNew) {
            // Trường hợp A: Lấy sản phẩm mới
            products = productService.getNewProducts();
        } else {
            // Trường hợp B: Lấy tất cả sản phẩm
            products = productService.getAllProducts();
        }

        return ResponseEntity.ok(products);
    }

    /**
     * API Chi Tiết Sản Phẩm (Product Detail)
     * URL: GET /api/product/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable Integer id) {
        Optional<ProductDTO> product = productService.getProductById(id);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

