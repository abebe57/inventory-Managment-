package org.example.inventorysysteamvo1.service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StockAdjustmentRequestDTO {

    @NotNull(message = "Quantity received is required")
    @Min(value = 1, message = "Quantity received must be at least 1")
    private Integer quantity;

    @NotNull(message = "Received date is required")
    @PastOrPresent(message = "Received date cannot be in the future")
    private LocalDate receivedDate;

    @NotNull(message = "Unit purchase price is required")
    @DecimalMin(value = "0.01", message = "Unit purchase price must be greater than zero")
    private BigDecimal unitPurchasePrice;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
