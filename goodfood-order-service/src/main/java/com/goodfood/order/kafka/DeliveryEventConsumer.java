package com.goodfood.order.kafka;

import com.goodfood.order.entity.Order;
import com.goodfood.order.entity.OrderStatus;
import com.goodfood.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "delivery-events",
            groupId = "order-service-group-delivery",
            containerFactory = "deliveryKafkaListenerContainerFactory"
    )
    public void consumeDeliveryEvent(Map<String, Object> event) {
        System.out.println("📦 Received delivery event: " + event);

        String eventType = (String) event.get("eventType");
        Long orderId = ((Number) event.get("orderId")).longValue();

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            System.err.println("⚠️ Order not found for ID: " + orderId);
            return;
        }

        Order order = optionalOrder.get();

        switch (eventType) {
            case "ORDER_PICKED_UP" -> order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
            case "ORDER_DELIVERED" -> order.setStatus(OrderStatus.COMPLETED);
            default -> System.out.println("ℹ️ Ignoring unknown delivery event type: " + eventType);
        }

        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        System.out.println("✅ Updated order " + orderId + " to status: " + order.getStatus());
    }
}
