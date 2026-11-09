package org.example.inventorysysteamvo1.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Records one stock receipt/batch for a product variant.
 * Purchase price is captured at the time the stock is received.
 */
public class StockReceipt {

    private final Long stockReceiptId;
    private final Long productId;
    private final Long supplierId;
    private final LocalDate receivedDate;
    private final int quantity;
    private final BigDecimal unitPurchasePrice;
    private final String notes;

    public StockReceipt(
            Long stockReceiptId,
            Long productId,
            Long supplierId,
            LocalDate receivedDate,
            int quantity,
            BigDecimal unitPurchasePrice,
            String notes) {

        if (productId == null) {
            throw new DomainValidationException("Product ID is required.");
        }
        if (supplierId == null) {
            throw new DomainValidationException("Supplier ID is required.");
        }
        if (receivedDate == null) {
            throw new DomainValidationException("Received date is required.");
        }
        if (receivedDate.isAfter(LocalDate.now())) {
            throw new DomainValidationException("Received date cannot be in the future.");
        }
        if (quantity <= 0) {
            throw new DomainValidationException("Received quantity must be greater than zero.");
        }
        if (unitPurchasePrice == null || unitPurchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("Unit purchase price must be greater than zero.");
        }
        if (notes != null && notes.trim().length() > 500) {
            throw new DomainValidationException("Receipt notes must not exceed 500 characters.");
        }

        this.stockReceiptId = stockReceiptId;
        this.productId = productId;
        this.supplierId = supplierId;
        this.receivedDate = receivedDate;
        this.quantity = quantity;
        this.unitPurchasePrice = unitPurchasePrice;
        this.notes = notes == null ? null : notes.trim();
    }

    public BigDecimal getTotalCost() {
        return unitPurchasePrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getStockReceiptId() { return stockReceiptId; }
    public Long getProductId() { return productId; }
    public Long getSupplierId() { return supplierId; }
    public LocalDate getReceivedDate() { return receivedDate; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPurchasePrice() { return unitPurchasePrice; }
    public String getNotes() { return notes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockReceipt other)) return false;
        return stockReceiptId != null && Objects.equals(stockReceiptId, other.stockReceiptId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stockReceiptId);
    }
}
