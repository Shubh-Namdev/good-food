package com.goodfood.delivery.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeliveryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "delivery-events";

    public void publishDeliveryStatus(Long deliveryId, Long orderId, String eventType) {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", orderId);
        event.put("deliveryId", deliveryId);
        event.put("eventType", eventType);

        kafkaTemplate.send(TOPIC, event);
        System.out.println("📤 Sent delivery event: " + event);
    }

}
