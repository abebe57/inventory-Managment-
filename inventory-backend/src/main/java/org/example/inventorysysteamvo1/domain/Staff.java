package org.example.inventorysysteamvo1.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public class Staff {

    // ── Constants ─────────────────────────────────────────────────────────
    private static final int     MIN_USERNAME_LENGTH = 4;
    private static final int     MAX_USERNAME_LENGTH = 50;
    private static final int     MIN_PASSWORD_LENGTH = 8;
    private static final int     MAX_NAME_LENGTH     = 100;
    private static final int     MAX_EMAIL_LENGTH    = 150;
    private static final int     MAX_PHONE_LENGTH    = 20;
    private static final int     MAX_ADDRESS_LENGTH  = 300;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{7,15}$");

    // ── Fields ────────────────────────────────────────────────────────────
    private final Long   staffId;
    private final String userName;   // immutable — username never changes after creation
    private final Long   roleId;     // controlled via assignRole()

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;

    // This field ALWAYS stores a BCrypt hash. The application service layer
    // is responsible for hashing BEFORE calling the constructor or changePassword().
    private String password;

    // ── Constructor ───────────────────────────────────────────────────────
    public Staff(Long staffId,
                 String firstName,
                 String lastName,
                 String email,
                 String userName,
                 String password,
                 Long roleId) {

        validateName(firstName, "First name");
        validateName(lastName,  "Last name");

        if (userName == null || userName.trim().length() < MIN_USERNAME_LENGTH) {
            throw new DomainValidationException(
                    "Username must be at least " + MIN_USERNAME_LENGTH + " characters.");
        }
        if (userName.trim().length() > MAX_USERNAME_LENGTH) {
            throw new DomainValidationException(
                    "Username must not exceed " + MAX_USERNAME_LENGTH + " characters.");
        }

        validateEmail(email);

        if (password == null || password.isBlank()) {
            throw new DomainValidationException("Password (hashed) is required.");
        }

        if (roleId == null) {
            throw new DomainValidationException("Role ID is required.");
        }

        this.staffId   = staffId;
        this.firstName = firstName.trim();
        this.lastName  = lastName.trim();
        this.email     = email.trim().toLowerCase();
        this.userName  = userName.trim();
        this.password  = password;
        this.roleId    = roleId;
    }

    // ── Business Methods ──────────────────────────────────────────────────

    /**
     * Accepts a NEW pre-hashed password.
     * The service layer must: (1) validate raw password strength,
     * (2) hash it, (3) call this method with the hash.
     */
    public void changePassword(String newHashedPassword) {
        if (newHashedPassword == null || newHashedPassword.isBlank()) {
            throw new DomainValidationException("New hashed password cannot be blank.");
        }
        if (newHashedPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new DomainValidationException("Password hash appears invalid (too short).");
        }
        this.password = newHashedPassword;
    }

    /**
     * Validates raw password strength BEFORE hashing.
     * Call this in the service layer, then hash, then call changePassword().
     */
    public static void validatePasswordStrength(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new DomainValidationException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
        boolean hasUpper = rawPassword.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = rawPassword.chars().anyMatch(Character::isDigit);
        if (!hasUpper || !hasDigit) {
            throw new DomainValidationException(
                    "Password must contain at least one uppercase letter and one digit.");
        }
    }

    /**
     * Updates mutable contact details.
     * Each parameter is optional — pass null to skip that field.
     */
    public void updateContactInfo(String phone, String address, String email) {
        if (phone != null) {
            if (!PHONE_PATTERN.matcher(phone.trim()).matches()) {
                throw new DomainValidationException("Invalid phone number format.");
            }
            if (phone.trim().length() > MAX_PHONE_LENGTH) {
                throw new DomainValidationException(
                        "Phone must not exceed " + MAX_PHONE_LENGTH + " characters.");
            }
            this.phone = phone.trim();
        }
        if (address != null) {
            if (address.trim().length() > MAX_ADDRESS_LENGTH) {
                throw new DomainValidationException(
                        "Address must not exceed " + MAX_ADDRESS_LENGTH + " characters.");
            }
            this.address = address.trim();
        }
        if (email != null) {
            validateEmail(email);
            this.email = email.trim().toLowerCase();
        }
    }

    /** Updates first and last name. */
    public void updateName(String firstName, String lastName) {
        validateName(firstName, "First name");
        validateName(lastName,  "Last name");
        this.firstName = firstName.trim();
        this.lastName  = lastName.trim();
    }

    /**
     * FIX — BUG in original:
     *   The original assignRole() called new Staff(...) with only constructor
     *   parameters and returned immediately. Because address and phone are NOT
     *   constructor parameters (they live in the optional updateContactInfo path),
     *   every role-change silently wiped those two columns from the database
     *   on the next save().
     *
     * FIX: after constructing the new instance, copy over the optional contact
     *   fields via updateContactInfo(). This is safe because updateContactInfo()
     *   skips any field that is null.
     */
    public Staff assignRole(Long newRoleId) {
        if (newRoleId == null) {
            throw new DomainValidationException("New role ID cannot be null.");
        }
        Staff reassigned = new Staff(
                this.staffId, this.firstName, this.lastName,
                this.email, this.userName, this.password, newRoleId);

        // FIX: carry over optional contact fields — the constructor does not accept them
        reassigned.updateContactInfo(this.phone, this.address, null);
        return reassigned;
    }

    /** Convenience display name. */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // ── Infrastructure-only Accessor ──────────────────────────────────────
    /**
     * FIX — MISSING in original:
     *   StaffRepositoryImpl.toEntity() and StaffServiceImpl.changePassword()
     *   both called a password getter that did not exist on this class, causing
     *   a compile error. Added here as an infrastructure-scoped accessor.
     *
     * IMPORTANT: This exposes the BCrypt hash for the REPOSITORY ADAPTER and
     * PASSWORD VERIFICATION only. NEVER expose this value in a controller
     * response, a DTO, or a log statement.
     */
    public String getPasswordHash() {
        return password;
    }

    // ── Private Validators ────────────────────────────────────────────────
    private static void validateName(String name, String fieldLabel) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainValidationException(fieldLabel + " is required.");
        }
        if (name.trim().length() > MAX_NAME_LENGTH) {
            throw new DomainValidationException(
                    fieldLabel + " must not exceed " + MAX_NAME_LENGTH + " characters.");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new DomainValidationException("Email is required.");
        }
        if (email.trim().length() > MAX_EMAIL_LENGTH) {
            throw new DomainValidationException(
                    "Email must not exceed " + MAX_EMAIL_LENGTH + " characters.");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new DomainValidationException("Invalid email format: " + email);
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public Long   getStaffId()   { return staffId; }
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getAddress()   { return address; }
    public String getPhone()     { return phone; }
    public String getEmail()     { return email; }
    public String getUserName()  { return userName; }
    public Long   getRoleId()    { return roleId; }

    // ── equals / hashCode (ID-based) ─────────────────────────────────────
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Staff other)) return false;
        return staffId != null && Objects.equals(staffId, other.staffId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(staffId);
    }

    // password intentionally excluded from toString()
    @Override
    public String toString() {
        return "Staff{id=" + staffId
                + ", name='" + getFullName() + '\''
                + ", userName='" + userName + '\''
                + ", email='" + email + '\''
                + ", roleId=" + roleId + '}';
    }
}