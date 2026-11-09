package org.example.inventorysysteamvo1.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private static final int LOW_STOCK_THRESHOLD = 10;

    private final Long productId;
    private String productName;
    private String productDescription;
    private String productUnit;
    private BigDecimal productPrice;
    private int productQuantity;
    private ProductStatus productStatus;
    private String otherDetails;
    private Long supplierId;
    private Long categoryId;

    public enum ProductStatus {
        IN_STOCK, LOW_STOCK, OUT_OF_STOCK, DISCONTINUED
    }

    public Product(
            Long productId,
            String productName,
            BigDecimal productPrice,
            int initialQuantity,
            Long supplierId,
            Long categoryId) {

        if (productName == null || productName.isBlank()) {
            throw new DomainValidationException("Product name cannot be blank.");
        }
        if (productPrice == null || productPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("Product price must be greater than zero.");
        }
        if (initialQuantity < 0) {
            throw new DomainValidationException("Product quantity cannot be negative.");
        }
        if (supplierId == null) {
            throw new DomainValidationException("Supplier ID is required.");
        }
        if (categoryId == null) {
            throw new DomainValidationException("Category ID is required.");
        }

        this.productId = productId;
        this.productName = productName.trim();
        this.productPrice = productPrice;
        this.productQuantity = initialQuantity;
        this.supplierId = supplierId;
        this.categoryId = categoryId;
        updateStatusBasedOnStock();
    }

    public Product withDescription(String description) {
        this.productDescription = description == null ? null : description.trim();
        return this;
    }

    public Product withUnit(String unit) {
        if (unit == null || unit.isBlank()) {
            throw new DomainValidationException("Product unit is required.");
        }
        this.productUnit = unit.trim();
        return this;
    }

    public Product withOtherDetails(String details) {
        this.otherDetails = details == null ? null : details.trim();
        return this;
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new DomainValidationException("Quantity to decrease must be greater than zero.");
        }
        if (productStatus == ProductStatus.DISCONTINUED) {
            throw new DomainValidationException("Cannot remove stock from a discontinued product.");
        }
        if (productQuantity < quantity) {
            throw new DomainValidationException(
                    "Insufficient stock for " + productName + " (" + productUnit + "). Available: " + productQuantity);
        }
        productQuantity -= quantity;
        updateStatusBasedOnStock();
    }

    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new DomainValidationException("Quantity to add must be greater than zero.");
        }
        if (productStatus == ProductStatus.DISCONTINUED) {
            throw new DomainValidationException("Cannot add stock to a discontinued product.");
        }
        productQuantity += quantity;
        updateStatusBasedOnStock();
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("New price must be greater than zero.");
        }
        this.productPrice = newPrice;
    }

    public void discontinue() {
        if (productStatus == ProductStatus.DISCONTINUED) {
            throw new DomainValidationException("Product is already discontinued.");
        }
        productStatus = ProductStatus.DISCONTINUED;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new DomainValidationException("Product name cannot be blank.");
        }
        productName = newName.trim();
    }

    /**
     * Updates catalog/master-data fields only.
     * Stock quantity and purchase cost are changed only through stock receipts
     * and stock-out orders, never through the product edit operation.
     */
    public void updateProductDetails(
            String newName,
            String newDescription,
            String newUnit,
            String newOtherDetails,
            Long newSupplierId,
            Long newCategoryId) {

        if (newName == null || newName.isBlank()) {
            throw new DomainValidationException("Product name is required.");
        }
        if (newUnit == null || newUnit.isBlank()) {
            throw new DomainValidationException("Product unit is required.");
        }
        if (newSupplierId == null || newCategoryId == null) {
            throw new DomainValidationException("Supplier ID and Category ID are required.");
        }

        productName = newName.trim();
        productDescription = newDescription == null ? null : newDescription.trim();
        productUnit = newUnit.trim();
        otherDetails = newOtherDetails == null ? null : newOtherDetails.trim();
        supplierId = newSupplierId;
        categoryId = newCategoryId;
        updateStatusBasedOnStock();
    }

    /**
     * Receives a new stock batch and recalculates weighted-average purchase cost.
     */
    public void receiveStock(int quantity, BigDecimal unitPurchasePrice) {
        if (quantity <= 0) {
            throw new DomainValidationException("Quantity to receive must be greater than zero.");
        }
        if (unitPurchasePrice == null || unitPurchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("Unit purchase price must be greater than zero.");
        }
        if (productStatus == ProductStatus.DISCONTINUED) {
            throw new DomainValidationException("Cannot receive stock for a discontinued product.");
        }

        BigDecimal oldStockValue = productPrice.multiply(BigDecimal.valueOf(productQuantity));
        BigDecimal newStockValue = unitPurchasePrice.multiply(BigDecimal.valueOf(quantity));
        int newQuantity = productQuantity + quantity;

        productPrice = oldStockValue
                .add(newStockValue)
                .divide(BigDecimal.valueOf(newQuantity), 2, java.math.RoundingMode.HALF_UP);

        productQuantity = newQuantity;
        updateStatusBasedOnStock();
    }
    private void updateStatusBasedOnStock() {
        if (productStatus == ProductStatus.DISCONTINUED) {
            return;
        }
        if (productQuantity == 0) {
            productStatus = ProductStatus.OUT_OF_STOCK;
        } else if (productQuantity < LOW_STOCK_THRESHOLD) {
            productStatus = ProductStatus.LOW_STOCK;
        } else {
            productStatus = ProductStatus.IN_STOCK;
        }
    }

    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductDescription() { return productDescription; }
    public String getProductUnit() { return productUnit; }
    public BigDecimal getProductPrice() { return productPrice; }
    public int getProductQuantity() { return productQuantity; }
    public ProductStatus getProductStatus() { return productStatus; }
    public String getOtherDetails() { return otherDetails; }
    public Long getSupplierId() { return supplierId; }
    public Long getCategoryId() { return categoryId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product other)) return false;
        return productId != null && Objects.equals(productId, other.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productId);
    }
}
