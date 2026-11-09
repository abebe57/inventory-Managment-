package org.example.inventorysysteamvo1.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class StockAdjustmentResponseDTO {
    private Long stockReceiptId;
    private Long productId;
    private String productName;
    private String productUnit;
    private LocalDate receivedDate;
    private Integer previousQuantity;
    private Integer addedQuantity;
    private Integer newQuantity;
    private BigDecimal unitPurchasePrice;
    private BigDecimal receiptTotalCost;
    private BigDecimal newAveragePurchaseCost;
}
