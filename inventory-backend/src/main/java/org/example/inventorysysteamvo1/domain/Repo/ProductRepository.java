package org.example.inventorysysteamvo1.domain.Repo;

import org.example.inventorysysteamvo1.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long productId);
    List<Product> findByProductName(String productName);
    Optional<Product> findByProductNameAndProductUnit(String productName, String productUnit);
    List<Product> findAll();
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findBySupplierId(Long supplierId);
    boolean existsById(Long productId);
    boolean existsByProductName(String productName);
    boolean existsByProductNameAndProductUnit(String productName, String productUnit);
    void deleteById(Long productId);
}
