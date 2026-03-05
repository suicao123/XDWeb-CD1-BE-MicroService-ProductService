package com.example.service;

import com.example.dto.CartItemDTO;
import com.example.dto.ProductDTO;
import com.example.entity.CartItem;
import com.example.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;

    public CartService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
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

