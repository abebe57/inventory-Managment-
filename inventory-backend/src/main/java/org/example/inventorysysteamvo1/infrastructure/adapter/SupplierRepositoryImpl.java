package org.example.inventorysysteamvo1.infrastructure.adapter;

import jakarta.persistence.EntityNotFoundException;
import org.example.inventorysysteamvo1.domain.Supplier;
import org.example.inventorysysteamvo1.domain.Repo.SupplierRepository;
import org.example.inventorysysteamvo1.infrastructure.SupplierEntity;
import org.example.inventorysysteamvo1.infrastructure.repository.SupplierJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SupplierRepositoryImpl implements SupplierRepository {

    private final SupplierJpaRepository jpaRepository;

    public SupplierRepositoryImpl(SupplierJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // ── Write Operations ───────────────────────────────────────────────

    @Override
    public Supplier save(Supplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Cannot save a null Supplier.");
        }

        SupplierEntity saved = jpaRepository.save(toEntity(supplier));
        return toDomain(saved);
    }

    @Override
    public void deleteById(Long supplierId) {
        if (supplierId == null) {
            throw new IllegalArgumentException("Supplier ID must not be null.");
        }

        if (!jpaRepository.existsById(supplierId)) {
            throw new EntityNotFoundException(
                    "Cannot delete: Supplier with ID [" + supplierId + "] was not found."
            );
        }

        jpaRepository.deleteById(supplierId);
    }

    // ── Read Operations ────────────────────────────────────────────────

    @Override
    public Optional<Supplier> findById(Long supplierId) {
        if (supplierId == null) {
            throw new IllegalArgumentException("Supplier ID must not be null.");
        }

        return jpaRepository.findById(supplierId)
                .map(this::toDomain);
    }

    @Override
    public Optional<Supplier> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Supplier name must not be blank.");
        }

        return jpaRepository.findByName(name.trim())
                .map(this::toDomain);
    }

    @Override
    public List<Supplier> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // ── Existence Checks ───────────────────────────────────────────────

    @Override
    public boolean existsById(Long supplierId) {
        if (supplierId == null) {
            throw new IllegalArgumentException("Supplier ID must not be null.");
        }

        return jpaRepository.existsById(supplierId);
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Supplier name must not be blank.");
        }

        return jpaRepository.existsByName(name.trim());
    }

    // ── Domain → Entity ────────────────────────────────────────────────

    private SupplierEntity toEntity(Supplier domain) {
        if (domain == null) {
            return null;
        }

        return SupplierEntity.builder()
                .supplierId(domain.getSupplierId())
                .name(domain.getName())
                .address(domain.getAddress())
                .phone(domain.getPhone())
                .fax(domain.getFax())
                .email(domain.getEmail())
                .otherDetails(domain.getOtherDetails())
                .build();
    }

    // ── Entity → Domain ────────────────────────────────────────────────

    private Supplier toDomain(SupplierEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Supplier(
                entity.getSupplierId(),
                entity.getName(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getFax(),
                entity.getEmail(),
                entity.getOtherDetails()
        );
    }
}