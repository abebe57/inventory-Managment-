package org.example.inventorysysteamvo1.infrastructure.repository;

import org.example.inventorysysteamvo1.infrastructure.StockReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockReceiptJpaRepository extends JpaRepository<StockReceiptEntity, Long> {
    List<StockReceiptEntity> findByProductIdOrderByReceivedDateDescStockReceiptIdDesc(Long productId);
    void deleteByProductId(Long productId);
}
