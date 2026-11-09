package org.example.inventorysysteamvo1.service;

import org.example.inventorysysteamvo1.service.dto.OrderItemRequestDTO;
import org.example.inventorysysteamvo1.service.dto.OrderRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.OrderResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);
    OrderResponseDTO updateOrder(Long orderId, OrderRequestDTO requestDTO);
    OrderResponseDTO getOrderById(Long orderId);
    List<OrderResponseDTO> getOrdersByDate(LocalDate date);
    List<OrderResponseDTO> getOrdersBetweenDates(LocalDate startDate, LocalDate endDate);
    List<OrderResponseDTO> getAllOrders();
    void addItemToOrder(Long orderId, OrderItemRequestDTO requestDTO);
    void updateItemInOrder(Long orderId, Long productId, OrderItemRequestDTO requestDTO);
    void removeItemFromOrder(Long orderId, Long productId);
    void deleteOrder(Long orderId);
}
