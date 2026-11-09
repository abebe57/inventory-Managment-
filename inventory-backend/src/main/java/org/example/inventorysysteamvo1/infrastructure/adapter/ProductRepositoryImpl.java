package org.example.inventorysysteamvo1.infrastructure.adapter;

import jakarta.persistence.EntityNotFoundException;
import org.example.inventorysysteamvo1.domain.Product;
import org.example.inventorysysteamvo1.domain.Repo.ProductRepository;
import org.example.inventorysysteamvo1.infrastructure.ProductEntity;
import org.example.inventorysysteamvo1.infrastructure.repository.ProductJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // ── Write Operations ───────────────────────────────────────────────

    @Override
    public Product save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Cannot save a null Product.");
        }

        ProductEntity saved = jpaRepository.save(toEntity(product));
        return toDomain(saved);
    }

    @Override
    public void deleteById(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must not be null.");
        }

        if (!jpaRepository.existsById(productId)) {
            throw new EntityNotFoundException(
                    "Cannot delete: Product with ID [" + productId + "] was not found."
            );
        }

        jpaRepository.deleteById(productId);
    }

    // ── Read Operations ────────────────────────────────────────────────

    @Override
    public Optional<Product> findById(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must not be null.");
        }

        return jpaRepository.findById(productId)
                .map(this::toDomain);
    }

    @Override
    public List<Product> findByProductName(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank.");
        }

        return jpaRepository.findByProductName(productName.trim())
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID must not be null.");
        }

        return jpaRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findBySupplierId(Long supplierId) {
        if (supplierId == null) {
            throw new IllegalArgumentException("Supplier ID must not be null.");
        }

        return jpaRepository.findBySupplierId(supplierId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // ── Existence Checks ───────────────────────────────────────────────

    @Override
    public boolean existsById(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must not be null.");
        }

        return jpaRepository.existsById(productId);
    }

    @Override
    public boolean existsByProductName(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank.");
        }
        return jpaRepository.existsByProductName(productName.trim());
    }

    @Override
    public Optional<Product> findByProductNameAndProductUnit(String productName, String productUnit) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank.");
        }
        if (productUnit == null || productUnit.isBlank()) {
            throw new IllegalArgumentException("Product unit must not be blank.");
        }
        return jpaRepository.findByProductNameAndProductUnit(productName.trim(), productUnit.trim())
                .map(this::toDomain);
    }

    @Override
    public boolean existsByProductNameAndProductUnit(String productName, String productUnit) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank.");
        }
        if (productUnit == null || productUnit.isBlank()) {
            throw new IllegalArgumentException("Product unit must not be blank.");
        }
        return jpaRepository.existsByProductNameAndProductUnit(productName.trim(), productUnit.trim());
    }

    // ── Domain → Entity ────────────────────────────────────────────────

    private ProductEntity toEntity(Product domain) {
        if (domain == null) {
            return null;
        }

        return ProductEntity.builder()
                .productId(domain.getProductId())
                .productName(domain.getProductName())
                .productDescription(domain.getProductDescription())
                .productUnit(domain.getProductUnit())
                .productPrice(domain.getProductPrice())
                .productQuantity(domain.getProductQuantity())
                .productStatus(domain.getProductStatus())
                .otherDetails(domain.getOtherDetails())
                .supplierId(domain.getSupplierId())
                .categoryId(domain.getCategoryId())
                .build();
    }

    // ── Entity → Domain ────────────────────────────────────────────────

    private Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Product(
                entity.getProductId(),
                entity.getProductName(),
                entity.getProductPrice(),
                entity.getProductQuantity(),
                entity.getSupplierId(),
                entity.getCategoryId()
        )
                .withDescription(entity.getProductDescription())
                .withUnit(entity.getProductUnit())
                .withOtherDetails(entity.getOtherDetails());
    }
}