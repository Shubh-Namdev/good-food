package com.goodfood.order.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.goodfood.order.events.OrderCreatedEvent;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // public void sendOrderEvent(Map<String, Object> event) {
    //     kafkaTemplate.send("order-events", event);
    //     System.out.println("✅ Published event to Kafka: " + event);
    // }

    public void sendOrderEvent(OrderCreatedEvent event) {
        kafkaTemplate.send("order-events", event);
        System.out.println("✅ Published event to Kafka: " + event);
    }
}
