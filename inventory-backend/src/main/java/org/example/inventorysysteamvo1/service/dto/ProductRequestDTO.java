package org.example.inventorysysteamvo1.service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Creates a new product variant and its first stock receipt.
 * productPrice is the initial purchase price, not a selling price.
 */
@Data
public class ProductRequestDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must not exceed 200 characters")
    private String productName;

    @NotBlank(message = "Product unit is required, e.g. 500ml, 1L, 2L")
    @Size(max = 50, message = "Product unit must not exceed 50 characters")
    private String productUnit;

    @NotNull(message = "Initial purchase price is required")
    @DecimalMin(value = "0.01", message = "Initial purchase price must be greater than zero")
    private BigDecimal productPrice;

    @NotNull(message = "Opening stock quantity is required")
    @Min(value = 1, message = "Opening stock quantity must be at least 1")
    private Integer productQuantity;

    @NotNull(message = "Initial received date is required")
    @PastOrPresent(message = "Initial received date cannot be in the future")
    private LocalDate receivedDate;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String productDescription;

    @Size(max = 500, message = "Other details must not exceed 500 characters")
    private String otherDetails;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
