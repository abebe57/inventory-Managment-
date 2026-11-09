package org.example.inventorysysteamvo1.infrastructure.repository;

import org.example.inventorysysteamvo1.infrastructure.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffJpaRepository extends JpaRepository<StaffEntity, Long> {
    Optional<StaffEntity> findByUserName(String userName);
    Optional<StaffEntity> findByEmail(String email);
    boolean existsByUserName(String userName); // ← ADD THIS
}