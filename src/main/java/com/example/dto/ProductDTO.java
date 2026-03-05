package com.example.dto;

// DTO chứa thông tin product trả về trong response
public class ProductDTO {

    private Integer id;
    private String productname;
    private Double price;
    private String image;

    public ProductDTO(Integer id, String productname, Double price, String image) {
        this.id = id;
        this.productname = productname;
        this.price = price;
        this.image = image;
    }

    // Constructor cũ để backward compatible
    public ProductDTO(String productname, Double price, String image) {
        this.productname = productname;
        this.price = price;
        this.image = image;
    }

    public Integer getId() { return id; }
    public String getProductname() { return productname; }
    public Double getPrice() { return price; }
    public String getImage() { return image; }
}

