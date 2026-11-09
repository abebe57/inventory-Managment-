package org.example.inventorysysteamvo1.service.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StockReceiptResponseDTO {
    private Long stockReceiptId;
    private Long productId;
    private Long supplierId;
    private LocalDate receivedDate;
    private Integer quantity;
    private BigDecimal unitPurchasePrice;
    private BigDecimal totalCost;
    private String notes;
}
