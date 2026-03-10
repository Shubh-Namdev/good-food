package com.goodfood.order.kafka;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.goodfood.order.events.OrderCreatedEvent;


@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    @Autowired
    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    // public void sendOrderEvent(Map<String, Object> event) {
    //     kafkaTemplate.send("order-events", event);
    //     System.out.println("✅ Published event to Kafka: " + event);
    // }

    public void sendOrderEvent(OrderCreatedEvent event) {
        kafkaTemplate.send("order-events", event);
        System.out.println("✅ Published event to Kafka: " + event);
    }
}
