package org.example.inventorysysteamvo1.infrastructure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.inventorysysteamvo1.domain.Product.ProductStatus;
import java.math.BigDecimal;
import java.util.Objects;
@Entity
@Table(name = "Product", uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_name_unit", columnNames = {"Product_Name", "Product_unit"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Product_ID", updatable = false, nullable = false)
    private Long productId;

    @Column(name = "Product_Name", length = 200, nullable = false)
    private String productName;

    @Column(name = "Product_description", length = 1000)
    private String productDescription;

    @Column(name = "Product_unit", length = 50)
    private String productUnit;

    @Column(name = "Product_Price", precision = 12, scale = 2, nullable = false)
    private BigDecimal productPrice;

    @Column(name = "Product_quantity", nullable = false)
    private Integer productQuantity;

    // Map the enum directly to a string representation in the DB
    @Enumerated(EnumType.STRING)
    @Column(name = "Product_Status", length = 30, nullable = false)
    private ProductStatus productStatus;

    @Column(name = "Other_Details", length = 500)
    private String otherDetails;

    // Crossing boundary to Supplier and Category aggregates via ID
    @Column(name = "Supplier_ID", nullable = false)
    private Long supplierId;

    @Column(name = "Category_ID", nullable = false)
    private Long categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductEntity that)) return false;
        return productId != null && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}