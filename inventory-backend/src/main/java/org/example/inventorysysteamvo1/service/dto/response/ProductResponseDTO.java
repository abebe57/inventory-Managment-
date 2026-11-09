package org.example.inventorysysteamvo1.service.dto.response;

import lombok.Data;
import org.example.inventorysysteamvo1.domain.Product.ProductStatus;

import java.math.BigDecimal;

@Data
public class ProductResponseDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private String productUnit;
    /** Current weighted-average purchase cost per unit. */
    private BigDecimal productPrice;
    private Integer productQuantity;
    private ProductStatus productStatus;
    private String otherDetails;
    private Long supplierId;
    private Long categoryId;
}
