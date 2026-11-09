package org.example.inventorysysteamvo1.infrastructure.adapter;

import jakarta.persistence.EntityNotFoundException;
import org.example.inventorysysteamvo1.domain.Order;
import org.example.inventorysysteamvo1.domain.OrderDetail;
import org.example.inventorysysteamvo1.domain.Repo.OrderRepository;
import org.example.inventorysysteamvo1.infrastructure.OrderDetailEntity;
import org.example.inventorysysteamvo1.infrastructure.OrderEntity;
import org.example.inventorysysteamvo1.infrastructure.repository.OrderJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order save(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Cannot save null Order.");
        }
        return toDomain(jpaRepository.save(toEntity(order)));
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return jpaRepository.findById(orderId).map(this::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Order> findByDateOfOrder(LocalDate dateOfOrder) {
        return jpaRepository.findByDateOfOrder(dateOfOrder).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Order> findByDateOfOrderBetween(LocalDate startDate, LocalDate endDate) {
        return jpaRepository.findByDateOfOrderBetween(startDate, endDate)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsById(Long orderId) {
        return jpaRepository.existsById(orderId);
    }

    @Override
    public void deleteById(Long orderId) {
        if (!jpaRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order with ID [" + orderId + "] not found.");
        }
        jpaRepository.deleteById(orderId);
    }

    private OrderEntity toEntity(Order order) {
        List<OrderDetailEntity> details = order.getItems().stream()
                .map(this::toDetailEntity)
                .toList();

        return OrderEntity.builder()
                .orderId(order.getOrderId())
                .dateOfOrder(order.getDateOfOrder())
                .orderDetails(order.getOrderDetails())
                .items(details)
                .build();
    }

    private OrderDetailEntity toDetailEntity(OrderDetail detail) {
        return OrderDetailEntity.builder()
                .orderDetailId(detail.getOrderDetailId())
                .productId(detail.getProductId())
                .quantity(detail.getQuantity())
                .date(detail.getDate())
                .build();
    }

    private Order toDomain(OrderEntity entity) {
        Order order = new Order(
                entity.getOrderId(),
                entity.getDateOfOrder(),
                entity.getOrderDetails()
        );
        if (entity.getItems() != null) {
            for (OrderDetailEntity detailEntity : entity.getItems()) {
                order.restoreItem(new OrderDetail(
                        detailEntity.getOrderDetailId(),
                        detailEntity.getProductId(),
                        detailEntity.getQuantity(),
                        detailEntity.getDate()
                ));
            }
        }
        return order;
    }
}
