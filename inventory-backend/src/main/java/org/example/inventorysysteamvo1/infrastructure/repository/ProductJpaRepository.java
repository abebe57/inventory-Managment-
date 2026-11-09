package org.example.inventorysysteamvo1.infrastructure.repository;

import org.example.inventorysysteamvo1.infrastructure.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByProductName(String productName);
    Optional<ProductEntity> findByProductNameAndProductUnit(String productName, String productUnit);
    boolean existsByProductName(String productName);
    boolean existsByProductNameAndProductUnit(String productName, String productUnit);
    List<ProductEntity> findByCategoryId(Long categoryId);
    List<ProductEntity> findBySupplierId(Long supplierId);
}
