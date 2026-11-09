package org.example.inventorysysteamvo1.infrastructure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects;
/**
 * Infrastructure Layer JPA Entity for the Customer table.
 * Maps directly to the database and is exclusively used by the JPA Repository.
 */
@Entity
@Table(name = "Customer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Customer_ID", updatable = false, nullable = false)
    private Long customerId;

    @Column(name = "First_Name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "Last_Name", length = 100)
    private String lastName;

    @Column(name = "Address", length = 300)
    private String address;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "Email", length = 150)
    private String email;

    // In DDD, we reference other aggregates by their ID rather than an object reference (@ManyToOne)
    // This maintains strict aggregate boundaries and improves query performance.
    @Column(name = "Staff_ID")
    private Long staffId;

    // ── Equality & HashCode (Primary Key Only) ───────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerEntity that)) return false;
        return customerId != null && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}