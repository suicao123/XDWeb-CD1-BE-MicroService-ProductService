package com.example.repository;

import com.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Lấy danh sách sản phẩm mới (is_new = 1) được sắp xếp theo ngày tạo giảm dần
    @Query(value = "SELECT * FROM products WHERE is_new = 1 ORDER BY id DESC LIMIT 4", nativeQuery = true)
    List<Product> findNewProducts();

    // Lấy tất cả sản phẩm (mặc định)
    @Query(value = "SELECT * FROM products", nativeQuery = true)
    List<Product> findAllProducts();

    // Tìm kiếm sản phẩm theo từ khóa trong productname hoặc description
    @Query(value = "SELECT * FROM products WHERE productname LIKE %?1% OR description LIKE %?1%", nativeQuery = true)
    List<Product> searchByKeyword(String keyword);
}

