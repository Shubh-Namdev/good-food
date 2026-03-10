package com.goodfood.restaurant.kafka;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestaurantEventProducer {

    @Autowired
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "restaurant-events";

    public void publishOrderStatus(Long orderId, String restaurantId, String eventType) {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", orderId);
        event.put("restaurantId", restaurantId);
        event.put("eventType", eventType);

        kafkaTemplate.send(TOPIC, event);
        System.out.println("📤 Sent restaurant event: " + event);
    }
}
