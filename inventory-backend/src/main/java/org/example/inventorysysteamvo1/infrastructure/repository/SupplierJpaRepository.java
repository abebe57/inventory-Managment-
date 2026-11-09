package org.example.inventorysysteamvo1.infrastructure.repository;
import org.example.inventorysysteamvo1.infrastructure.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SupplierJpaRepository extends JpaRepository<SupplierEntity, Long> {
    Optional<SupplierEntity> findByName(String name); // ← ADD THIS
    boolean existsByName(String name); // ← ADD THIS
}