package org.example.inventorysysteamvo1.domain;
import java.util.Objects;

public class Role {

    // ── Constants ────────────────────────────────────────────────────────
    private static final int MAX_ROLE_NAME_LENGTH  = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 500;

    // ── Fields ───────────────────────────────────────────────────────────
    private final Long   roleId;    // immutable identity
    private String roleName;  // ✅ FIX 3: final — role name is set once at creation

    private String description;     // mutable — can be updated via business method

    // ── Constructor ──────────────────────────────────────────────────────
    public Role(Long roleId, String roleName, String description) {

        // ✅ FIX 1 + FIX 2: trim AND validate length before storing
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new DomainValidationException("Role name is required.");
        }
        String trimmedName = roleName.trim();
        if (trimmedName.length() > MAX_ROLE_NAME_LENGTH) {
            throw new DomainValidationException(
                    "Role name must not exceed " + MAX_ROLE_NAME_LENGTH + " characters.");
        }

        // ✅ FIX 8: guard description length
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new DomainValidationException(
                    "Description must not exceed " + MAX_DESCRIPTION_LENGTH + " characters.");
        }

        this.roleId      = roleId;
        this.roleName    = trimmedName;   // ✅ stored trimmed
        this.description = description;
    }

    // ── Business Methods ─────────────────────────────────────────────────

    // ✅ FIX 5: controlled mutation for description
    /**
     * Updates the role description.
     * Role name is intentionally immutable after creation.
     */
    public void updateDescription(String newDescription) {
        if (newDescription != null && newDescription.length() > MAX_DESCRIPTION_LENGTH) {
            throw new DomainValidationException(
                    "Description must not exceed " + MAX_DESCRIPTION_LENGTH + " characters.");
        }
        this.description = newDescription;
    }
    /**
     * Updates the mutable details of this role.
     */
    public void updateRoleDetails(String newRoleName, String newDescription) {
        if (newRoleName == null || newRoleName.trim().isEmpty()) {
            throw new DomainValidationException("Role name is required.");
        }
        String trimmedName = newRoleName.trim();
        if (trimmedName.length() > MAX_ROLE_NAME_LENGTH) {
            throw new DomainValidationException(
                    "Role name must not exceed " + MAX_ROLE_NAME_LENGTH + " characters.");
        }
        if (newDescription != null && newDescription.length() > MAX_DESCRIPTION_LENGTH) {
            throw new DomainValidationException(
                    "Description must not exceed " + MAX_DESCRIPTION_LENGTH + " characters.");
        }
        this.roleName = trimmedName;
        this.description = newDescription != null ? newDescription.trim() : null;
    }
    /**
     * Business query: checks whether this is the system admin role.
     * Useful for guard checks in services (e.g. prevent deleting ADMIN).
     */
    public boolean isAdminRole() {
        return "ADMIN".equalsIgnoreCase(this.roleName);
    }

    // ── Getters ──────────────────────────────────────────────────────────
    public Long   getRoleId()      { return roleId; }
    public String getRoleName()    { return roleName; }
    public String getDescription() { return description; }

    // ── equals / hashCode (ID-based, per DDD entity rules) ───────────────
    // ✅ FIX 4 + FIX 7
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role other)) return false;
        return roleId != null && Objects.equals(roleId, other.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roleId);
    }

    // ✅ FIX 6
    @Override
    public String toString() {
        return "Role{id=" + roleId
                + ", name='" + roleName + '\''
                + ", description='" + description + "'}";
    }
}