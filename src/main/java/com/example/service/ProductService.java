package com.example.service;

import com.example.dto.ProductDTO;
import com.example.entity.Product;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Lấy danh sách sản phẩm mới
     */
    public List<ProductDTO> getNewProducts() {
        List<Product> products = productRepository.findNewProducts();
        return convertToDTO(products);
    }

    /**
     * Lấy danh sách tất cả sản phẩm
     */
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAllProducts();
        return convertToDTO(products);
    }

    /**
     * Lấy chi tiết sản phẩm theo ID
     */
    public Optional<ProductDTO> getProductById(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(p -> new ProductDTO(
                p.getId(),
                p.getProductname(),
                p.getPrice(),
                p.getImage(),
                p.getDescription(),
                p.getStatus()
        ));
    }

    /**
     * Chuyển đổi từ Product entity sang ProductDTO
     */
    private List<ProductDTO> convertToDTO(List<Product> products) {
        return products.stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getProductname(),
                        product.getPrice(),
                        product.getImage()
                ))
                .collect(Collectors.toList());
    }
}

