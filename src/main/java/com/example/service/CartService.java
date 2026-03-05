package com.example.service;

import com.example.dto.CartItemDTO;
import com.example.dto.ProductDTO;
import com.example.entity.CartItem;
import com.example.entity.Product;
import com.example.repository.CartItemRepository;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addToCart(Integer userId, Integer productId, Integer quantity) {
        // Kiểm tra sản phẩm có tồn tại
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
        CartItem existingItem = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElse(null);

        if (existingItem != null) {
            // Nếu đã có, cập nhật số lượng
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            // Nếu chưa có, tạo mới
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng theo id
    public void removeCartItem(Integer id) {
        cartItemRepository.deleteById(id);
    }

    // Lấy danh sách giỏ hàng theo userId
    public List<CartItemDTO> getCartByUserId(Integer userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);

        return items.stream().map(item -> {
            ProductDTO productDTO = new ProductDTO(
                    item.getProduct().getProductname(),
                    item.getProduct().getPrice(),
                    item.getProduct().getImage()
            );
            return new CartItemDTO(item.getId(), item.getQuantity(), productDTO);
        }).collect(Collectors.toList());
    }
}

