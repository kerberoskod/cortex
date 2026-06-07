package com.cortex.order.service;

import com.cortex.common.dto.CreateOrderRequest;
import com.cortex.common.dto.OrderDTO;
import com.cortex.common.dto.OrderItemDTO;
import com.cortex.common.event.OrderCreatedEvent;
import com.cortex.order.model.Order;
import com.cortex.order.model.OrderItem;
import com.cortex.order.repository.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public OrderDTO createOrder(Long userId, CreateOrderRequest request) {
        var items = request.getItems().stream()
                .map(i -> new OrderItem(i.getProductId(), i.getProductName(), i.getQuantity(), i.getPrice()))
                .toList();

        var total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var order = new Order(userId, "CREATED", total, items);
        order = orderRepository.save(order);

        eventPublisher.publishEvent(new OrderCreatedEvent(order.getId(), userId, total));

        return toDTO(order);
    }

    public OrderDTO getOrderById(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return toDTO(order);
    }

    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    public OrderDTO shipOrder(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus("SHIPPED");
        order = orderRepository.save(order);

        return toDTO(order);
    }

    private OrderDTO toDTO(Order order) {
        var items = order.getItems().stream()
                .map(i -> new OrderItemDTO(i.getProductId(), i.getProductName(), i.getQuantity(), i.getPrice()))
                .toList();
        return new OrderDTO(order.getId(), order.getUserId(), order.getStatus(),
                order.getTotal(), items, order.getCreatedAt());
    }
}
