package org.example.inventorysysteamvo1.service.dto.response.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.inventorysysteamvo1.service.OrderService;
import org.example.inventorysysteamvo1.service.dto.OrderItemRequestDTO;
import org.example.inventorysysteamvo1.service.dto.OrderRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.OrderResponseDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Internal stock-out order management")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create a stock-out order and reduce product quantities")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(requestDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a stock-out order")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    @Operation(summary = "Get all stock-out orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get stock-out orders for a date")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(orderService.getOrdersByDate(date));
    }

    @GetMapping("/between")
    @Operation(summary = "Get stock-out orders between two dates")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(orderService.getOrdersBetweenDates(start, end));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update stock-out order notes/date without changing stock")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequestDTO requestDTO) {
        return ResponseEntity.ok(orderService.updateOrder(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order and return its quantities to stock")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/items")
    @Operation(summary = "Add a product to an existing stock-out order and reduce stock")
    public ResponseEntity<Void> addItemToOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequestDTO requestDTO) {
        orderService.addItemToOrder(orderId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{orderId}/items/{productId}")
    @Operation(summary = "Change a product quantity in an order and adjust stock")
    public ResponseEntity<Void> updateItemInOrder(
            @PathVariable Long orderId,
            @PathVariable Long productId,
            @Valid @RequestBody OrderItemRequestDTO requestDTO) {
        orderService.updateItemInOrder(orderId, productId, requestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{orderId}/items/{productId}")
    @Operation(summary = "Remove a product from an order and return its quantity to stock")
    public ResponseEntity<Void> removeItemFromOrder(
            @PathVariable Long orderId,
            @PathVariable Long productId) {
        orderService.removeItemFromOrder(orderId, productId);
        return ResponseEntity.noContent().build();
    }
}
