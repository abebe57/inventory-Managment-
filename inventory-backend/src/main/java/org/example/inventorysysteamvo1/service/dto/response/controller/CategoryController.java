
        package org.example.inventorysysteamvo1.service.dto.response.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.inventorysysteamvo1.service.CategoryService;
import org.example.inventorysysteamvo1.service.dto.CategoryRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.CategoryResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Product category management")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ── Create ────────────────────────────────────────────────────────────

    /**
     * POST /api/categories
     */
    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO requestDTO) {

        CategoryResponseDTO created = categoryService.createCategory(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ── Read ──────────────────────────────────────────────────────────────

    /**
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a category by ID")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    /**
     * GET /api/categories
     */
    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // ── Update ────────────────────────────────────────────────────────────

    /**
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO requestDTO) {

        return ResponseEntity.ok(categoryService.updateCategory(id, requestDTO));
    }

    // ── Delete ────────────────────────────────────────────────────────────

    /**
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category by ID")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

