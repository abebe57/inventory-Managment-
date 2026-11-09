package org.example.inventorysysteamvo1.infrastructure.adapter;
import jakarta.persistence.EntityNotFoundException;
import org.example.inventorysysteamvo1.domain.Customer;
import org.example.inventorysysteamvo1.domain.Repo.CustomerRepository;
import org.example.inventorysysteamvo1.infrastructure.CustomerEntity;
import org.example.inventorysysteamvo1.infrastructure.repository.CustomerJpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    // ── Dependencies ──────────────────────────────────────────────────────

    // Note: To avoid naming conflicts with your Domain interface,
    // it's best to name the injected JPA interface distinctively.
    private final CustomerJpaRepository jpaRepository;

    public CustomerRepositoryImpl(CustomerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // ── Write Operations ──────────────────────────────────────────────────

    @Override
    public Customer save(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Cannot save a null Customer.");
        }

        CustomerEntity entity = toEntity(customer);
        CustomerEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID must not be null for deletion.");
        }

        if (!jpaRepository.existsById(customerId)) {
            throw new EntityNotFoundException(
                    "Cannot delete: Customer with ID [" + customerId + "] was not found.");
        }

        jpaRepository.deleteById(customerId);
    }

    // ── Read Operations ───────────────────────────────────────────────────

    @Override
    public Optional<Customer> findById(Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID must not be null for lookup.");
        }

        return jpaRepository.findById(customerId)
                .map(this::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // ── Existence Checks ──────────────────────────────────────────────────

    @Override
    public boolean existsById(Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID must not be null for existence check.");
        }

        return jpaRepository.existsById(customerId);
    }

    @Override
    public Optional<Customer> findByPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone cannot be null or blank.");
        }

        return jpaRepository.findByPhone(phone)
                .map(this::toDomain);
    }


    @Override
    public Optional<Customer> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }

        return jpaRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public List<Customer> findByStaffId(Long staffId) {
        if (staffId == null) {
            throw new IllegalArgumentException("Staff ID cannot be null.");
        }

        return jpaRepository.findByStaffId(staffId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsByPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone cannot be null or blank.");
        }

        return jpaRepository.existsByPhone(phone);
    }


    // ── Private Inline Mapping Methods ────────────────────────────────────

    /**
     * Converts the Domain model → JPA Entity for saving to the database.
     */
    private CustomerEntity toEntity(Customer customer) {
        if (customer == null) return null;

        return CustomerEntity.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .staffId(customer.getStaffId())
                .build();
    }

    /**
     * Converts the JPA Entity → Domain model after loading from the database.
     * Uses the domain constructor so all business validation rules still apply.
     */
    private Customer toDomain(CustomerEntity entity) {
        if (entity == null) return null;

        return new Customer(
                entity.getCustomerId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getStaffId()
        );
    }
}