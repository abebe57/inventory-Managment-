package org.example.inventorysysteamvo1.infrastructure.repository;

import org.example.inventorysysteamvo1.infrastructure.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName); // ← ADD THIS
}