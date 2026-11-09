package org.example.inventorysysteamvo1.infrastructure.adapter;

import jakarta.persistence.EntityNotFoundException;
import org.example.inventorysysteamvo1.domain.Repo.StaffRepository;
import org.example.inventorysysteamvo1.domain.Staff;
import org.example.inventorysysteamvo1.infrastructure.StaffEntity;
import org.example.inventorysysteamvo1.infrastructure.repository.StaffJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class StaffRepositoryImpl implements StaffRepository {

    private final StaffJpaRepository jpaRepository;

    public StaffRepositoryImpl(StaffJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Staff save(Staff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Cannot save a null Staff.");
        }

        StaffEntity saved = jpaRepository.save(toEntity(staff));
        return toDomain(saved);
    }

    @Override
    public Optional<Staff> findById(Long staffId) {
        if (staffId == null) {
            throw new IllegalArgumentException("Staff ID must not be null.");
        }
        return jpaRepository.findById(staffId).map(this::toDomain);
    }

    @Override
    public Optional<Staff> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank.");
        }
        return jpaRepository.findByUserName(username.trim()).map(this::toDomain);
    }

    @Override
    public List<Staff> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsById(Long staffId) {
        if (staffId == null) {
            throw new IllegalArgumentException("Staff ID must not be null.");
        }
        return jpaRepository.existsById(staffId);
    }

    @Override
    public boolean existsByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank.");
        }
        return jpaRepository.existsByUserName(username.trim());
    }

    @Override
    public void deleteById(Long staffId) {
        if (staffId == null) {
            throw new IllegalArgumentException("Staff ID must not be null.");
        }
        if (!jpaRepository.existsById(staffId)) {
            throw new EntityNotFoundException(
                    "Staff with ID [" + staffId + "] was not found.");
        }
        jpaRepository.deleteById(staffId);
    }

    private StaffEntity toEntity(Staff domain) {
        return StaffEntity.builder()
                .staffId(domain.getStaffId())
                .firstName(domain.getFirstName())
                .lastName(domain.getLastName())
                .address(domain.getAddress())
                .phone(domain.getPhone())
                .email(domain.getEmail())
                .userName(domain.getUserName())
                .password(domain.getPasswordHash())
                .roleId(domain.getRoleId())
                .build();
    }

    private Staff toDomain(StaffEntity entity) {
        Staff domain = new Staff(
                entity.getStaffId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getUserName(),
                entity.getPassword(),
                entity.getRoleId()
        );
        domain.updateContactInfo(entity.getPhone(), entity.getAddress(), null);
        return domain;
    }
}
