package com.goodfood.order.service;

import com.goodfood.order.dto.OrderRequest;
import com.goodfood.order.entity.*;
import com.goodfood.order.kafka.OrderEventProducer;
import com.goodfood.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    } 

    public Order createOrder(String customerId, OrderRequest dto) {
        Order order = Order.builder()
                .customerId(customerId)
                .restaurantId(dto.getRestaurantId())
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(dto.getItems().stream()
                        .map(item -> OrderItem.builder()
                                .menuItemId(item.getMenuItemId())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        BigDecimal totalAmount = new BigDecimal(0);
        order.getItems().forEach(i -> {
                totalAmount.add(i.getPrice().multiply(new BigDecimal(i.getQuantity())));
                i.setOrder(order);
        });
        order.setTotalAmount(totalAmount);
        Order saved = orderRepository.save(order);

        // Publish to Kafka
        HashMap<String, Object> event = new HashMap<>();
        event.put("eventType", OrderStatus.CREATED);
        event.put("orderId", saved.getId());
        event.put("restaurantId", saved.getRestaurantId());
        event.put("customerId", saved.getCustomerId());
        eventProducer.sendOrderEvent(event);

        return saved;
    }
}
