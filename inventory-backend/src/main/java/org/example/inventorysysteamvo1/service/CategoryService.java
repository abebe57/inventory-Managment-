package org.example.inventorysysteamvo1.service;
import org.example.inventorysysteamvo1.service.dto.CategoryRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.CategoryResponseDTO;
import java.util.List;
public interface CategoryService {

    // ── Create & Update ───────────────────────────────────────────────────

    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);

    CategoryResponseDTO updateCategory(Long categoryId, CategoryRequestDTO requestDTO);

    // ── Read ──────────────────────────────────────────────────────────────

    CategoryResponseDTO getCategoryById(Long categoryId);

    List<CategoryResponseDTO> getAllCategories();

    // ── Delete ────────────────────────────────────────────────────────────

    void deleteCategory(Long categoryId);
}