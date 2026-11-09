package org.example.inventorysysteamvo1.service.mapper;

import org.example.inventorysysteamvo1.domain.Order;
import org.example.inventorysysteamvo1.domain.OrderDetail;
import org.example.inventorysysteamvo1.service.dto.OrderItemRequestDTO;
import org.example.inventorysysteamvo1.service.dto.OrderRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.OrderDetailResponseDTO;
import org.example.inventorysysteamvo1.service.dto.response.OrderResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapper {

    public Order toDomain(OrderRequestDTO dto) {
        Order order = new Order(null, dto.getDateOfOrder(), dto.getOrderDetails());
        if (dto.getItems() != null) {
            for (OrderItemRequestDTO item : dto.getItems()) {
                order.addOrUpdateItem(item.getProductId(), item.getQuantity());
            }
        }
        return order;
    }

    public OrderResponseDTO toResponse(Order order) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setDateOfOrder(order.getDateOfOrder());
        response.setOrderDetails(order.getOrderDetails());
        response.setItems(order.getItems().stream().map(this::toDetailResponse).toList());
        return response;
    }

    private OrderDetailResponseDTO toDetailResponse(OrderDetail detail) {
        OrderDetailResponseDTO response = new OrderDetailResponseDTO();
        response.setOrderDetailId(detail.getOrderDetailId());
        response.setProductId(detail.getProductId());
        response.setQuantity(detail.getQuantity());
        response.setDate(detail.getDate());
        return response;
    }
}
