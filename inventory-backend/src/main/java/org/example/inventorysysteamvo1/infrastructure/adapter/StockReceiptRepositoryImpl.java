package org.example.inventorysysteamvo1.infrastructure.adapter;

import org.example.inventorysysteamvo1.domain.Repo.StockReceiptRepository;
import org.example.inventorysysteamvo1.domain.StockReceipt;
import org.example.inventorysysteamvo1.infrastructure.StockReceiptEntity;
import org.example.inventorysysteamvo1.infrastructure.repository.StockReceiptJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockReceiptRepositoryImpl implements StockReceiptRepository {

    private final StockReceiptJpaRepository jpaRepository;

    public StockReceiptRepositoryImpl(StockReceiptJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public StockReceipt save(StockReceipt receipt) {
        if (receipt == null) {
            throw new IllegalArgumentException("Cannot save null stock receipt.");
        }
        return toDomain(jpaRepository.save(toEntity(receipt)));
    }

    @Override
    public List<StockReceipt> findByProductId(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must not be null.");
        }
        return jpaRepository.findByProductIdOrderByReceivedDateDescStockReceiptIdDesc(productId)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteByProductId(Long productId) {
        if (productId != null) {
            jpaRepository.deleteByProductId(productId);
        }
    }

    private StockReceiptEntity toEntity(StockReceipt receipt) {
        return StockReceiptEntity.builder()
                .stockReceiptId(receipt.getStockReceiptId())
                .productId(receipt.getProductId())
                .supplierId(receipt.getSupplierId())
                .receivedDate(receipt.getReceivedDate())
                .quantity(receipt.getQuantity())
                .unitPurchasePrice(receipt.getUnitPurchasePrice())
                .totalCost(receipt.getTotalCost())
                .notes(receipt.getNotes())
                .build();
    }

    private StockReceipt toDomain(StockReceiptEntity entity) {
        return new StockReceipt(
                entity.getStockReceiptId(),
                entity.getProductId(),
                entity.getSupplierId(),
                entity.getReceivedDate(),
                entity.getQuantity(),
                entity.getUnitPurchasePrice(),
                entity.getNotes()
        );
    }
}
