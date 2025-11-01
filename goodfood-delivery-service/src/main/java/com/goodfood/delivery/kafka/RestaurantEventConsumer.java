package com.goodfood.delivery.kafka;

import com.goodfood.delivery.entity.Delivery;
import com.goodfood.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestaurantEventConsumer {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryEventProducer eventProducer;

    @KafkaListener(topics = "restaurant-events", groupId = "delivery-service-group-restaurant")
    public void handleRestaurantEvent(Map<String, Object> event) {
        System.out.println("📥 Received restaurant event: " + event);

        String eventType = (String) event.get("eventType");
        Long orderId = ((Number) event.get("orderId")).longValue();

        if ("ORDER_READY".equals(eventType)) {
            // Simulate assigning delivery
            Delivery delivery = Delivery.builder()
                    .orderId(orderId)
                    .deliveryAgentId("AGENT_" + orderId)
                    .status("OUT_FOR_DELIVERY")
                    .assignedAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Delivery deliveryDetails = deliveryRepository.save(delivery);
            System.out.println("🚚 Delivery assigned for order: " + orderId);

            // Publish delivery event to Kafka
            eventProducer.publishDeliveryStatus(deliveryDetails.getId(), deliveryDetails.getOrderId() ,"ORDER_PICKED_UP");
        }
    }
}
