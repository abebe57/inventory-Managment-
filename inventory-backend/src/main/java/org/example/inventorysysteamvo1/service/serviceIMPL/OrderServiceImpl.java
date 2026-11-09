package org.example.inventorysysteamvo1.service.serviceIMPL;

import jakarta.transaction.Transactional;
import org.example.inventorysysteamvo1.domain.DomainValidationException;
import org.example.inventorysysteamvo1.domain.Order;
import org.example.inventorysysteamvo1.domain.Repo.OrderRepository;
import org.example.inventorysysteamvo1.domain.Repo.ProductRepository;
import org.example.inventorysysteamvo1.domain.Product;
import org.example.inventorysysteamvo1.service.OrderService;
import org.example.inventorysysteamvo1.service.dto.OrderItemRequestDTO;
import org.example.inventorysysteamvo1.service.dto.OrderRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.OrderDetailResponseDTO;
import org.example.inventorysysteamvo1.service.dto.response.OrderResponseDTO;
import org.example.inventorysysteamvo1.service.mapper.OrderDtoMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDtoMapper dtoMapper;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            OrderDtoMapper dtoMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        if (requestDTO == null || requestDTO.getItems() == null || requestDTO.getItems().isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least one product.");
        }

        Map<Long, Integer> quantitiesByProduct = new HashMap<>();
        for (OrderItemRequestDTO item : requestDTO.getItems()) {
            quantitiesByProduct.merge(item.getProductId(), item.getQuantity(), Integer::sum);
        }

        // Validate all stock before changing anything.
        for (Map.Entry<Long, Integer> entry : quantitiesByProduct.entrySet()) {
            Product product = getProduct(entry.getKey());
            int requested = entry.getValue();
            if (product.getProductQuantity() < requested) {
                throw new DomainValidationException(
                        "Insufficient stock for " + product.getProductName() + " (" + product.getProductUnit() +
                                "). Available: " + product.getProductQuantity() + ", requested: " + requested);
            }
        }

        Order order = dtoMapper.toDomain(requestDTO);

        for (Map.Entry<Long, Integer> entry : quantitiesByProduct.entrySet()) {
            Product product = getProduct(entry.getKey());
            product.decreaseStock(entry.getValue());
            productRepository.save(product);
        }

        return toResponseWithStock(orderRepository.save(order));
    }

    @Override
    public OrderResponseDTO updateOrder(Long orderId, OrderRequestDTO requestDTO) {
        Order order = getOrder(orderId);
        order.updateOrderInfo(requestDTO.getDateOfOrder(), requestDTO.getOrderDetails());
        return toResponseWithStock(orderRepository.save(order));
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        return toResponseWithStock(getOrder(orderId));
    }

    @Override
    public List<OrderResponseDTO> getOrdersByDate(LocalDate date) {
        return orderRepository.findByDateOfOrder(date).stream().map(this::toResponseWithStock).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersBetweenDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("A valid start and end date are required.");
        }
        return orderRepository.findByDateOfOrderBetween(startDate, endDate)
                .stream().map(this::toResponseWithStock).toList();
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toResponseWithStock).toList();
    }

    @Override
    public void addItemToOrder(Long orderId, OrderItemRequestDTO requestDTO) {
        Order order = getOrder(orderId);
        Product product = getProduct(requestDTO.getProductId());
        product.decreaseStock(requestDTO.getQuantity());
        productRepository.save(product);
        order.addOrUpdateItem(requestDTO.getProductId(), requestDTO.getQuantity());
        orderRepository.save(order);
    }

    @Override
    public void updateItemInOrder(Long orderId, Long productId, OrderItemRequestDTO requestDTO) {
        Order order = getOrder(orderId);
        Product product = getProduct(productId);

        int oldQuantity = order.getItemQuantity(productId);
        int newQuantity = requestDTO.getQuantity();

        if (newQuantity > oldQuantity) {
            product.decreaseStock(newQuantity - oldQuantity);
        } else if (newQuantity < oldQuantity) {
            product.addStock(oldQuantity - newQuantity);
        }

        productRepository.save(product);
        order.setItemQuantity(productId, newQuantity);
        orderRepository.save(order);
    }

    @Override
    public void removeItemFromOrder(Long orderId, Long productId) {
        Order order = getOrder(orderId);
        Product product = getProduct(productId);
        int quantity = order.getItemQuantity(productId);
        product.addStock(quantity);
        productRepository.save(product);
        order.removeItem(productId);
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = getOrder(orderId);
        for (var item : order.getItems()) {
            Product product = getProduct(item.getProductId());
            product.addStock(item.getQuantity());
            productRepository.save(product);
        }
        orderRepository.deleteById(orderId);
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productId + " does not exist."));
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order with ID " + orderId + " not found."));
    }

    private OrderResponseDTO toResponseWithStock(Order order) {
        OrderResponseDTO response = dtoMapper.toResponse(order);
        for (OrderDetailResponseDTO item : response.getItems()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                item.setProductName(product.getProductName());
                item.setProductUnit(product.getProductUnit());
                item.setRemainingStock(product.getProductQuantity());
            }
        }
        return response;
    }
}
