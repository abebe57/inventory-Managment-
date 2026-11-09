package org.example.inventorysysteamvo1.domain.Repo;

import org.example.inventorysysteamvo1.domain.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long orderId);
    List<Order> findAll();
    List<Order> findByDateOfOrder(LocalDate dateOfOrder);
    List<Order> findByDateOfOrderBetween(LocalDate startDate, LocalDate endDate);
    boolean existsById(Long orderId);
    void deleteById(Long orderId);
}
