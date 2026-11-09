package org.example.inventorysysteamvo1.service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderRequestDTO {

    @NotNull(message = "Order date is required")
    private LocalDate dateOfOrder;

    @Size(max = 1000, message = "Order details must not exceed 1000 characters")
    private String orderDetails;

    @Valid
    @NotEmpty(message = "Add at least one product to the order")
    private List<OrderItemRequestDTO> items = new ArrayList<>();
}
