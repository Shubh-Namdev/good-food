package com.goodfood.restaurant.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderEventListener {

    @KafkaListener(topics = "order-events", groupId = "restaurant-service-group-order")
    public void handleOrderEvent(Map<String, Object> orderEvent) {
        System.out.println("📥 Received order event in Restaurant Service: " + orderEvent);

        String eventType = (String) orderEvent.get("eventType");
        Long orderId = Long.valueOf(orderEvent.get("orderId").toString());
        String restaurantId = (String) orderEvent.get("restaurantId");

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
