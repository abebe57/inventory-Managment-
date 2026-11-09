package org.example.inventorysysteamvo1.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * One stock-out line inside an order.
 * Price, payment and bill numbers are intentionally not part of this model.
 */
public class OrderDetail {

    private final Long orderDetailId;
    private final Long productId;
    private final int quantity;
    private final LocalDateTime date;

    public OrderDetail(
            Long orderDetailId,
            Long productId,
            int quantity,
            LocalDateTime date) {

        if (productId == null) {
            throw new DomainValidationException("Product ID is required.");
        }
        if (quantity <= 0) {
            throw new DomainValidationException("Quantity must be greater than zero.");
        }

        this.orderDetailId = orderDetailId;
        this.productId = productId;
        this.quantity = quantity;
        this.date = date == null ? LocalDateTime.now() : date;
    }

    public Long getOrderDetailId() { return orderDetailId; }
    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public LocalDateTime getDate() { return date; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetail other)) return false;
        return orderDetailId != null && Objects.equals(orderDetailId, other.orderDetailId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderDetailId);
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderDetailId=" + orderDetailId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", date=" + date +
                '}';
    }
}
