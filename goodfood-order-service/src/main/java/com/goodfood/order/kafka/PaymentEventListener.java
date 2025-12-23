package com.goodfood.order.kafka;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.goodfood.order.entity.Order;
import com.goodfood.order.entity.OrderStatus;
import com.goodfood.order.events.PaymentEvent;
import com.goodfood.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentEventListener {
    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "payment-events",
            groupId = "payment-service-group",
            containerFactory = "paymentKafkaListenerContainerFactory"
    )
    public void consumePaymentEvent(PaymentEvent event) {
        System.out.println("📦 Received payment event: " + event);

        String eventType = event.getEventType();
        Long orderId = event.getOrderId();

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            System.err.println("⚠️ Order not found for ID: " + orderId);
            return;
        }

        Order order = optionalOrder.get();

        switch (eventType) {
            case "PAYMENT_SUCCESS" -> order.setStatus(OrderStatus.ORDER_ACCEPTED);
            case "PAYMENT_DECLINED" -> order.setStatus(OrderStatus.ORDER_REJECTED);
            default -> System.out.println("ℹ️ Ignoring unknown delivery event type: " + eventType);
        }

        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        System.out.println("✅ Updated order " + orderId + " to status: " + order.getStatus());

    }
}
