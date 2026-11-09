package org.example.inventorysysteamvo1.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Inventory stock-out order.
 * An order records products removed from stock; it is not a sales/payment document.
 */
public class Order {

    private static final int MAX_DETAILS_LENGTH = 1000;

    private final Long orderId;
    private LocalDate dateOfOrder;
    private String orderDetails;
    private final List<OrderDetail> items = new ArrayList<>();

    public Order(
            Long orderId,
            LocalDate dateOfOrder,
            String orderDetails) {

        if (dateOfOrder == null) {
            throw new DomainValidationException("Order date is required.");
        }
        if (dateOfOrder.isAfter(LocalDate.now())) {
            throw new DomainValidationException("Order date cannot be in the future.");
        }
        validateOrderDetails(orderDetails);

        this.orderId = orderId;
        this.dateOfOrder = dateOfOrder;
        this.orderDetails = orderDetails == null ? null : orderDetails.trim();
    }

    public void addOrUpdateItem(Long productId, int quantity) {
        if (productId == null) {
            throw new DomainValidationException("Product ID is required.");
        }
        if (quantity <= 0) {
            throw new DomainValidationException("Quantity must be greater than zero.");
        }

        for (int i = 0; i < items.size(); i++) {
            OrderDetail existing = items.get(i);
            if (Objects.equals(existing.getProductId(), productId)) {
                OrderDetail replacement = new OrderDetail(
                        existing.getOrderDetailId(),
                        productId,
                        existing.getQuantity() + quantity,
                        existing.getDate()
                );
                items.set(i, replacement);
                return;
            }
        }

        items.add(new OrderDetail(null, productId, quantity, null));
    }

    public OrderDetail findItem(Long productId) {
        return items.stream()
                .filter(item -> Objects.equals(item.getProductId(), productId))
                .findFirst()
                .orElseThrow(() -> new DomainValidationException(
                        "Order item for product ID " + productId + " was not found."
                ));
    }

    public int getItemQuantity(Long productId) {
        return findItem(productId).getQuantity();
    }

    public void setItemQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            throw new DomainValidationException("Quantity must be greater than zero.");
        }
        for (int i = 0; i < items.size(); i++) {
            OrderDetail existing = items.get(i);
            if (Objects.equals(existing.getProductId(), productId)) {
                items.set(i, new OrderDetail(
                        existing.getOrderDetailId(),
                        productId,
                        quantity,
                        existing.getDate()
                ));
                return;
            }
        }
        throw new DomainValidationException("Order item for product ID " + productId + " was not found.");
    }

    public void removeItem(Long productId) {
        boolean removed = items.removeIf(item -> Objects.equals(item.getProductId(), productId));
        if (!removed) {
            throw new DomainValidationException("Order item for product ID " + productId + " was not found.");
        }
    }

    public boolean containsItem(Long productId) {
        return items.stream().anyMatch(item -> Objects.equals(item.getProductId(), productId));
    }

    public void updateOrderInfo(LocalDate newDateOfOrder, String newOrderDetails) {
        if (newDateOfOrder == null) {
            throw new DomainValidationException("Order date is required.");
        }
        if (newDateOfOrder.isAfter(LocalDate.now())) {
            throw new DomainValidationException("Order date cannot be in the future.");
        }
        validateOrderDetails(newOrderDetails);
        this.dateOfOrder = newDateOfOrder;
        this.orderDetails = newOrderDetails == null ? null : newOrderDetails.trim();
    }

    public void restoreItem(OrderDetail item) {
        if (item == null) {
            throw new DomainValidationException("Order detail cannot be null.");
        }
        items.add(item);
    }

    private static void validateOrderDetails(String details) {
        if (details != null && details.trim().length() > MAX_DETAILS_LENGTH) {
            throw new DomainValidationException(
                    "Order details must not exceed " + MAX_DETAILS_LENGTH + " characters."
            );
        }
    }

    public Long getOrderId() { return orderId; }
    public LocalDate getDateOfOrder() { return dateOfOrder; }
    public String getOrderDetails() { return orderDetails; }
    public List<OrderDetail> getItems() { return Collections.unmodifiableList(items); }
}
