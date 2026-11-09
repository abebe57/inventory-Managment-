package org.example.inventorysysteamvo1.infrastructure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects;
@Entity
@Table(name = "Supplier")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Supplier_ID", updatable = false, nullable = false)
    private Long supplierId;

    @Column(name = "Name", length = 200, nullable = false)
    private String name;

    @Column(name = "Address", length = 400)
    private String address;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "fax", length = 20)
    private String fax;

    @Column(name = "Email", length = 150)
    private String email;

    @Column(name = "other_details", length = 500)
    private String otherDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupplierEntity that)) return false;
        return supplierId != null && Objects.equals(supplierId, that.supplierId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
