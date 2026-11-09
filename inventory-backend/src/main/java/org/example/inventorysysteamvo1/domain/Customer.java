package org.example.inventorysysteamvo1.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {
    // ── Constants ───────────────────────────────────────────────────
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_ADDRESS_LENGTH = 300;
    private static final int MAX_PHONE_LENGTH = 20;
    private static final int MAX_EMAIL_LENGTH = 150;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{7,15}$");

    // ── Fields ──────────────────────────────────────────────────────
    private final Long customerId;

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
    private Long staffId;

    // ── Constructor ─────────────────────────────────────────────────
    public Customer(Long customerId,
                    String firstName,
                    String lastName,
                    String address,
                    String phone,
                    String email,
                    Long staffId) {

        this.customerId = customerId;

        this.firstName = normalizeRequired(firstName, "First name");
        validateName(this.firstName, "First name");

        this.lastName = normalizeOptional(lastName);

        if (this.lastName != null) {
            validateName(this.lastName, "Last name");
        }

        this.address = normalizeOptional(address);
        validateLength(
                this.address,
                MAX_ADDRESS_LENGTH,
                "Address"
        );

        this.phone = normalizeRequired(phone, "Phone number");
        validatePhone(this.phone);

        this.email = normalizeOptional(email);

        if (this.email != null) {
            this.email = this.email.toLowerCase();
            validateEmail(this.email);
        }

        this.staffId = staffId;
    }

    // ── Convenience Constructor ────────────────────────────────────
    public Customer(Long customerId,
                    String firstName,
                    String phone,
                    Long staffId) {

        this(
                customerId,
                firstName,
                null,
                null,
                phone,
                null,
                staffId
        );
    }

    // ── Business Methods ───────────────────────────────────────────

    public void updateName(String firstName, String lastName) {

        String normalizedFirst =
                normalizeRequired(firstName, "First name");

        validateName(normalizedFirst, "First name");

        String normalizedLast =
                normalizeOptional(lastName);

        if (normalizedLast != null) {
            validateName(normalizedLast, "Last name");
        }

        this.firstName = normalizedFirst;
        this.lastName = normalizedLast;
    }

    public void updateContactDetails(String phone,
                                     String email,
                                     String address) {

        if (phone != null) {

            String normalizedPhone =
                    normalizeRequired(phone, "Phone number");

            validatePhone(normalizedPhone);

            this.phone = normalizedPhone;
        }

        if (email != null) {

            String normalizedEmail =
                    normalizeOptional(email);

            if (normalizedEmail != null) {

                normalizedEmail =
                        normalizedEmail.toLowerCase();

                validateEmail(normalizedEmail);
            }

            this.email = normalizedEmail;
        }

        if (address != null) {

            String normalizedAddress =
                    normalizeOptional(address);

            validateLength(
                    normalizedAddress,
                    MAX_ADDRESS_LENGTH,
                    "Address"
            );

            this.address = normalizedAddress;
        }
    }

    public void assignStaff(Long staffId) {
        this.staffId = staffId;
    }

    public String getFullName() {

        return (lastName != null)
                ? firstName + " " + lastName
                : firstName;
    }

    // ── Normalization Helpers ──────────────────────────────────────

    private static String normalizeRequired(
            String value,
            String fieldLabel) {

        if (value == null || value.trim().isEmpty()) {

            throw new DomainValidationException(
                    fieldLabel + " is required."
            );
        }

        return value.trim();
    }

    private static String normalizeOptional(String value) {

        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty() ? null : trimmed;
    }

    // ── Validators ─────────────────────────────────────────────────

    private static void validateName(
            String name,
            String fieldLabel) {

        if (name.length() > MAX_NAME_LENGTH) {

            throw new DomainValidationException(
                    fieldLabel + " must not exceed "
                            + MAX_NAME_LENGTH
                            + " characters."
            );
        }
    }

    private static void validatePhone(String phone) {

        if (phone.length() > MAX_PHONE_LENGTH) {

            throw new DomainValidationException(
                    "Phone number must not exceed "
                            + MAX_PHONE_LENGTH
                            + " characters."
            );
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {

            throw new DomainValidationException(
                    "Invalid phone number format."
            );
        }
    }

    private static void validateEmail(String email) {

        if (email.length() > MAX_EMAIL_LENGTH) {

            throw new DomainValidationException(
                    "Email must not exceed "
                            + MAX_EMAIL_LENGTH
                            + " characters."
            );
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {

            throw new DomainValidationException(
                    "Invalid email format: " + email
            );
        }
    }

    private static void validateLength(
            String value,
            int max,
            String fieldLabel) {

        if (value != null && value.length() > max) {

            throw new DomainValidationException(
                    fieldLabel + " must not exceed "
                            + max
                            + " characters."
            );
        }
    }

    // ── Getters ────────────────────────────────────────────────────

    public Long getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Long getStaffId() {
        return staffId;
    }

    // ── Equality ───────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Customer other)) {
            return false;
        }

        return Objects.equals(
                customerId,
                other.customerId
        );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(customerId);
    }

    // ── Debugging ──────────────────────────────────────────────────

    @Override
    public String toString() {

        return "Customer{id=" + customerId
                + ", fullName='" + getFullName() + '\''
                + ", phone='" + phone + '\''
                + ", email='" + email + '\''
                + ", staffId=" + staffId
                + '}';
    }
}