package org.example.inventorysysteamvo1.infrastructure.repository;

import org.example.inventorysysteamvo1.infrastructure.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByDateOfOrder(LocalDate dateOfOrder);

    List<OrderEntity> findByDateOfOrderBetween(LocalDate startDate, LocalDate endDate);
}
