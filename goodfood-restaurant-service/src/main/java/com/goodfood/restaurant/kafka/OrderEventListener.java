package com.goodfood.restaurant.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.goodfood.restaurant.events.OrderCreatedEvent;

@Service
public class OrderEventListener {

    @KafkaListener(
        topics = "order-events", 
        groupId = "order-service-group",
        containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void handleOrderEvent(OrderCreatedEvent orderEvent) {
        System.out.println("📥 Received order event in Restaurant Service: " + orderEvent);

        String eventType = orderEvent.getEventType();
        Long orderId = orderEvent.getOrderId();
        String restaurantId = orderEvent.getRestaurantId();

        switch (eventType) {
            case "ORDER_CREATED":
                System.out.println("🍽️ New order received by restaurant " + restaurantId +
                        ": Order #" + orderId);
                // TODO: Save order to restaurant DB, notify kitchen, etc.
                break;
            case "ORDER_CANCELLED":
                System.out.println("🚫 Order cancelled: " + orderId);
                // TODO: Update internal DB
                break;
            default:
                System.out.println("⚠️ Unknown event type: " + eventType);
        }
    }
}
