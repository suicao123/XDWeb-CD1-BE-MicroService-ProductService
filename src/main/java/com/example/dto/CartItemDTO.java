package com.example.dto;

// DTO trả về cho mỗi item trong giỏ hàng
public class CartItemDTO {

    private Integer id;
    private Integer quantity;
    private ProductDTO product;

    public CartItemDTO(Integer id, Integer quantity, ProductDTO product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public Integer getId() { return id; }
    public Integer getQuantity() { return quantity; }
    public ProductDTO getProduct() { return product; }
}

