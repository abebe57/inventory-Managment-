package org.example.inventorysysteamvo1.domain;
import java.util.Objects;
import java.util.regex.Pattern;

public class Supplier {

    // ── Constants ────────────────────────────────────────────────────────
    private static final int MAX_NAME_LENGTH         = 200;
    private static final int MAX_ADDRESS_LENGTH      = 400;
    private static final int MAX_PHONE_LENGTH        = 20;
    private static final int MAX_EMAIL_LENGTH        = 150;
    private static final int MAX_OTHER_DETAILS_LENGTH = 500;

    // ✅ FIX 1: stronger, standard email regex
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    // ✅ FIX 2: phone pattern added
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{7,15}$");

    // ── Fields ───────────────────────────────────────────────────────────
    private final Long   supplierId;  // ✅ FIX 8: immutable identity
    private String name;
    private String address;
    private String phone;
    private String fax;
    private String email;
    private String otherDetails;

    // ── Constructor ──────────────────────────────────────────────────────
    public Supplier(Long supplierId,
                    String name,
                    String address,
                    String phone,
                    String fax,
                    String email,
                    String otherDetails) {

        // ✅ FIX 4: static validators — no instance state needed in constructor
        validateName(name);
        validatePhone(phone, "Supplier phone");   // phone is required
        validateFax(fax);                         // ✅ FIX 3: fax validated
        validateEmail(email);                     // email validated

        validateLength(address,      MAX_ADDRESS_LENGTH,       "Address");
        validateLength(otherDetails, MAX_OTHER_DETAILS_LENGTH, "Other details");

        this.supplierId   = supplierId;
        this.name         = name.trim();                              // ✅ FIX 5
        this.address      = (address != null) ? address.trim() : null;
        this.phone        = phone.trim();
        this.fax          = (fax   != null)  ? fax.trim()   : null;
        this.email        = (email != null)  ? email.trim().toLowerCase() : null;
        this.otherDetails = otherDetails;
    }

    // ── Business Methods ─────────────────────────────────────────────────

    // ✅ FIX 10: dedicated name update
    /** Updates the supplier's trading name. */
    public void updateName(String newName) {
        validateName(newName);
        this.name = newName.trim();
    }

    /**
     * Updates contact details independently.
     * ✅ FIX 6 + FIX 12: each field is optional — only non-null values are applied.
     * Pass null to leave a field unchanged.
     */
    public void updateContactDetails(String phone, String email, String address) {

        if (phone != null) {
            validatePhone(phone, "Supplier phone");
            this.phone = phone.trim();
        }
        if (email != null) {
            validateEmail(email);
            this.email = email.trim().toLowerCase();
        }
        if (address != null) {
            validateLength(address, MAX_ADDRESS_LENGTH, "Address");
            this.address = address.trim();
        }
    }

    /** Updates fax number independently. Pass null to clear it. */
    public void updateFax(String fax) {
        validateFax(fax);
        this.fax = (fax != null) ? fax.trim() : null;
    }

    /** Updates supplementary details. */
    public void updateOtherDetails(String otherDetails) {
        validateLength(otherDetails, MAX_OTHER_DETAILS_LENGTH, "Other details");
        this.otherDetails = otherDetails;
    }

    // ── Private Static Validators ────────────────────────────────────────
    // ✅ FIX 4: all static — safe to call from constructor

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainValidationException("Supplier name is required.");
        }
        if (name.trim().length() > MAX_NAME_LENGTH) {
            throw new DomainValidationException(
                    "Supplier name must not exceed " + MAX_NAME_LENGTH + " characters.");
        }
    }

    // ✅ FIX 2: phone format enforced
    private static void validatePhone(String phone, String fieldLabel) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new DomainValidationException(fieldLabel + " is required.");
        }
        if (phone.trim().length() > MAX_PHONE_LENGTH) {
            throw new DomainValidationException(
                    fieldLabel + " must not exceed " + MAX_PHONE_LENGTH + " characters.");
        }
        if (!PHONE_PATTERN.matcher(phone.trim()).matches()) {
            throw new DomainValidationException(
                    "Invalid " + fieldLabel + " format. Use digits only, optionally starting with +.");
        }
    }

    // ✅ FIX 3: fax validated (optional field — null is allowed)
    private static void validateFax(String fax) {
        if (fax == null) return;   // fax is optional
        if (fax.trim().length() > MAX_PHONE_LENGTH) {
            throw new DomainValidationException(
                    "Fax must not exceed " + MAX_PHONE_LENGTH + " characters.");
        }
        if (!PHONE_PATTERN.matcher(fax.trim()).matches()) {
            throw new DomainValidationException(
                    "Invalid fax number format. Use digits only, optionally starting with +.");
        }
    }

    // ✅ FIX 1: strong email regex — null is allowed (email is optional for supplier)
    private static void validateEmail(String email) {
        if (email == null) return;
        if (email.trim().length() > MAX_EMAIL_LENGTH) {
            throw new DomainValidationException(
                    "Email must not exceed " + MAX_EMAIL_LENGTH + " characters.");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new DomainValidationException(
                    "Invalid supplier email format: " + email);
        }
    }

    private static void validateLength(String value, int max, String fieldLabel) {
        if (value != null && value.length() > max) {
            throw new DomainValidationException(
                    fieldLabel + " must not exceed " + max + " characters.");
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────
    // ✅ FIX 7: all getters present
    public Long   getSupplierId()   { return supplierId; }
    public String getName()         { return name; }
    public String getAddress()      { return address; }
    public String getPhone()        { return phone; }
    public String getFax()          { return fax; }
    public String getEmail()        { return email; }
    public String getOtherDetails() { return otherDetails; }

    // ── equals / hashCode (ID-based) ──────────────────────────────────────
    // ✅ FIX 9
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supplier other)) return false;
        return Objects.equals(supplierId, other.supplierId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(supplierId);
    }

    // ✅ FIX 13
    @Override
    public String toString() {
        return "Supplier{id=" + supplierId
                + ", name='" + name + '\''
                + ", phone='" + phone + '\''
                + ", email='" + email + '\''
                + ", fax='" + fax + "'}";
    }
}