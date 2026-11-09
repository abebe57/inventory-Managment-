package org.example.inventorysysteamvo1.service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDetailResponseDTO {

    private Long orderDetailId;
    private Long productId;
    private String productName;
    private String productUnit;
    private Integer quantity;
    private Integer remainingStock;
    private LocalDateTime date;
}
