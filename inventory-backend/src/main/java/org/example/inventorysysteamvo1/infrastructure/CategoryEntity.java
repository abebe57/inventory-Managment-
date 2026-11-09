package org.example.inventorysysteamvo1.infrastructure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects;
/**
 * Infrastructure Layer JPA Entity for the Category table.
 * This class maps directly to the database and should only be used by Repositories.
 */
@Entity
@Table(name = "Category") // Matches the schema table name
@Data                     // Lombok: Generates getters, setters, toString, equals, hashCode
@Builder                  // Lombok: Generates the builder pattern
@NoArgsConstructor        // Lombok: Required by JPA
@AllArgsConstructor       // Lombok: Used by the Builder
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Category_ID", updatable = false, nullable = false)
    private Long categoryId;

    @Column(name = "Category_Name", length = 150, nullable = false, unique = true)
    private String categoryName;

    @Column(name = "Description", length = 500)
    private String description;

    // ── Equality & HashCode (Critical for JPA) ───────────────────────
    // Lombok's @Data includes all fields in equals/hashCode by default.
    // In JPA, it is a strict best practice to ONLY use the Primary Key for entity equality.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryEntity that)) return false;
        return categoryId != null && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}