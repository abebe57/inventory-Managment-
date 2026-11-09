package org.example.inventorysysteamvo1.infrastructure.adapter;

import jakarta.persistence.EntityNotFoundException;
import org.example.inventorysysteamvo1.domain.Category;
import org.example.inventorysysteamvo1.domain.Repo.CategoryRepository;
import org.example.inventorysysteamvo1.infrastructure.CategoryEntity;
import org.example.inventorysysteamvo1.infrastructure.repository.CategoryJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;

    public CategoryRepositoryImpl(CategoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // ── Write Operations ──────────────────────────────────────────────────

    @Override
    public Category save(Category category) {
        CategoryEntity entity = toEntity(category);
        CategoryEntity saved  = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(Long categoryId) {
        if (!jpaRepository.existsById(categoryId)) {
            throw new EntityNotFoundException(
                    "Category with ID [" + categoryId + "] was not found.");
        }
        jpaRepository.deleteById(categoryId);
    }

    // ── Read Operations ───────────────────────────────────────────────────

    @Override
    public Optional<Category> findById(Long categoryId) {
        return jpaRepository.findById(categoryId)
                .map(this::toDomain);
    }

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        return jpaRepository.findByCategoryName(categoryName)
                .map(this::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // ── Existence Checks ──────────────────────────────────────────────────

    @Override
    public boolean existsById(Long categoryId) {
        return jpaRepository.existsById(categoryId);
    }

    @Override
    public boolean existsByCategoryName(String categoryName) {
        return jpaRepository.existsByCategoryName(categoryName);
    }

    // ── Private Inline Mapping Methods ───────────────────────────────────

    /**
     * Converts the Domain model → JPA Entity for saving to the database.
     */
    private CategoryEntity toEntity(Category category) {
        return CategoryEntity.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }

    /**
     * Converts the JPA Entity → Domain model after loading from the database.
     * Uses the domain constructor so all business validation rules still apply.
     */
    private Category toDomain(CategoryEntity entity) {
        return new Category(
                entity.getCategoryId(),
                entity.getCategoryName(),
                entity.getDescription()
        );
    }
}