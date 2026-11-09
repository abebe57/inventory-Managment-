package org.example.inventorysysteamvo1.domain.Repo;

import org.example.inventorysysteamvo1.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(Long categoryId);

    Optional<Category> findByCategoryName(String categoryName);

    List<Category> findAll();

    boolean existsById(Long categoryId);

    boolean existsByCategoryName(String categoryName);

    void deleteById(Long categoryId);
}