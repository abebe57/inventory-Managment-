package org.example.inventorysysteamvo1.domain;
import java.util.Objects;

public class Category {

    // ── Constants ───────────────────────────────────────────────────
    private static final int MAX_NAME_LENGTH = 150;
    private static final int MAX_DESCRIPTION_LENGTH = 500;

    // ── Fields ──────────────────────────────────────────────────────
    private final Long categoryId;
    private String categoryName;
    private String description;

    // ── Constructor ─────────────────────────────────────────────────
    public Category(Long categoryId, String categoryName, String description) {

        this.categoryId = categoryId;
        this.categoryName = validateAndTrimCategoryName(categoryName);
        this.description = normalizeDescription(description);
    }

    // ── Business Methods ────────────────────────────────────────────

    public void renameCategory(String newName) {
        this.categoryName = validateAndTrimCategoryName(newName);
    }

    public void updateDescription(String newDescription) {
        this.description = normalizeDescription(newDescription);
    }

    // ── Validation ──────────────────────────────────────────────────

    private static String validateAndTrimCategoryName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new DomainValidationException(
                    "Category name cannot be empty.");
        }

        String trimmed = name.trim();

        if (trimmed.length() > MAX_NAME_LENGTH) {
            throw new DomainValidationException(
                    "Category name must not exceed "
                            + MAX_NAME_LENGTH + " characters.");
        }

        return trimmed;
    }

    private static String normalizeDescription(String description) {

        if (description == null) {
            return null;
        }

        String trimmed = description.trim();

        if (trimmed.length() > MAX_DESCRIPTION_LENGTH) {
            throw new DomainValidationException(
                    "Description must not exceed "
                            + MAX_DESCRIPTION_LENGTH + " characters.");
        }

        return trimmed.isEmpty() ? null : trimmed;
    }
    /**
     * Updates the category details.
     * Enforces domain validation rules before allowing the update.
     */
    public void updateCategory(String newName, String newDescription) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new DomainValidationException("Category name cannot be empty.");
        }
        if (newName.trim().length() > MAX_NAME_LENGTH) {
            throw new DomainValidationException("Category name must not exceed " + MAX_NAME_LENGTH + " characters.");
        }

        if (newDescription != null && newDescription.trim().length() > MAX_DESCRIPTION_LENGTH) {
            throw new DomainValidationException("Description must not exceed " + MAX_DESCRIPTION_LENGTH + " characters.");
        }

        this.categoryName = newName.trim();
        this.description = newDescription != null ? newDescription.trim() : null;
    }


    // ── Getters ─────────────────────────────────────────────────────

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    // ── Equality ────────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Category other)) return false;

        return categoryId != null && Objects.equals(categoryId, other.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categoryId);
    }

    // ── Debugging ───────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Category{id=" + categoryId +
                ", name='" + categoryName + '\'' +
                ", description=" + description + '}';
    }
}