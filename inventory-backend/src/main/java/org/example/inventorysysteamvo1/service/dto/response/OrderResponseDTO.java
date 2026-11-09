package org.example.inventorysysteamvo1.service.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long orderId;
    private LocalDate dateOfOrder;
    private String orderDetails;
    private List<OrderDetailResponseDTO> items;
}
