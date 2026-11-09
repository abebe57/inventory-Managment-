package org.example.inventorysysteamvo1.domain.Repo;

import org.example.inventorysysteamvo1.domain.StockReceipt;

import java.util.List;

public interface StockReceiptRepository {
    StockReceipt save(StockReceipt receipt);
    List<StockReceipt> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
