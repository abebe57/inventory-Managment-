package org.example.inventorysysteamvo1.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductUpdateRequestDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must not exceed 200 characters")
    private String productName;

    @NotBlank(message = "Product unit is required, e.g. 500ml, 1L, 2L")
    @Size(max = 50, message = "Product unit must not exceed 50 characters")
    private String productUnit;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String productDescription;

    @Size(max = 500, message = "Other details must not exceed 500 characters")
    private String otherDetails;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
