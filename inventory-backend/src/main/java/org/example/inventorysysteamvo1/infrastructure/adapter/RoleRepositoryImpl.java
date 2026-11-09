package org.example.inventorysysteamvo1.infrastructure.adapter;
import jakarta.persistence.EntityNotFoundException;
import org.example.inventorysysteamvo1.domain.Role;
import org.example.inventorysysteamvo1.domain.Repo.RoleRepository;
import org.example.inventorysysteamvo1.infrastructure.RoleEntity;
import org.example.inventorysysteamvo1.infrastructure.repository.RoleJpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository jpaRepository;

    public RoleRepositoryImpl(RoleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // ── Write Operations ──────────────────────────────────────────────────

    @Override
    public Role save(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Cannot save a null Role.");
        }
        RoleEntity saved = jpaRepository.save(toEntity(role));
        return toDomain(saved);
    }

    @Override
    public void deleteById(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("Role ID must not be null.");
        }
        if (!jpaRepository.existsById(roleId)) {
            throw new EntityNotFoundException(
                    "Cannot delete: Role with ID [" + roleId + "] was not found.");
        }
        jpaRepository.deleteById(roleId);
    }

    // ── Read Operations ───────────────────────────────────────────────────

    @Override
    public Optional<Role> findById(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("Role ID must not be null.");
        }
        return jpaRepository.findById(roleId)
                .map(this::toDomain);
    }

    @Override
    public Optional<Role> findByRoleName(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("Role name must not be blank.");
        }
        return jpaRepository.findByRoleName(roleName.trim())
                .map(this::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // ── Existence Checks ──────────────────────────────────────────────────

    @Override
    public boolean existsById(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("Role ID must not be null.");
        }
        return jpaRepository.existsById(roleId);
    }

    @Override
    public boolean existsByRoleName(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("Role name must not be blank.");
        }
        return jpaRepository.existsByRoleName(roleName.trim());
    }

    // ── Private Inline Mapping Methods (NO EXTERNAL MAPPER) ───────────────

    private RoleEntity toEntity(Role domain) {
        if (domain == null) return null;
        return RoleEntity.builder()
                .roleId(domain.getRoleId())
                .roleName(domain.getRoleName())
                .description(domain.getDescription())
                .build();
    }

    private Role toDomain(RoleEntity entity) {
        if (entity == null) return null;
        return new Role(
                entity.getRoleId(),
                entity.getRoleName(),
                entity.getDescription()
        );
    }
}