package com.example.dto;

// DTO chứa thông tin product trả về trong response giỏ hàng
public class ProductDTO {

    private String productname;
    private Double price;
    private String image;

    public ProductDTO(String productname, Double price, String image) {
        this.productname = productname;
        this.price = price;
        this.image = image;
    }

    public String getProductname() { return productname; }
    public Double getPrice() { return price; }
    public String getImage() { return image; }
}

