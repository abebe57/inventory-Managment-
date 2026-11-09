package org.example.inventorysysteamvo1.infrastructure.repository;

import org.example.inventorysysteamvo1.infrastructure.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {

    boolean existsByPhone(String phone);

    Optional<CustomerEntity> findByPhone(String phone);

    Optional<CustomerEntity> findByEmail(String email);

    List<CustomerEntity> findByStaffId(Long staffId);
}