package org.example.inventorysysteamvo1.infrastructure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects;
@Entity
@Table(name = "Staff")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Staff_ID", updatable = false, nullable = false)
    private Long staffId;

    @Column(name = "First_Name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "Last_Name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "Address", length = 300)
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "Email", length = 150)
    private String email;

    @Column(name = "UserName", length = 50, nullable = false, unique = true)
    private String userName;

    // Based on the DB Schema typo 'PasseWord'
    @Column(name = "PasseWord", nullable = false)
    private String password;

    // Crossing boundary to Role aggregate via ID
    @Column(name = "Role_ID", nullable = false)
    private Long roleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StaffEntity that)) return false;
        return staffId != null && Objects.equals(staffId, that.staffId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}