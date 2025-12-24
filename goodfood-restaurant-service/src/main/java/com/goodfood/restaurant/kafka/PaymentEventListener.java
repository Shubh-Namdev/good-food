package com.goodfood.restaurant.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.goodfood.restaurant.events.PaymentEvent;

@Service
public class PaymentEventListener {

    @KafkaListener(
        topics = "payment-events", 
        groupId = "restaurant-service-group",
        containerFactory = "paymentKafkaListenerContainerFactory"
    )
    public void handleOrderEvent(PaymentEvent paymentEvent) {
        System.out.println("📥 Received payment event in Restaurant Service: " + paymentEvent);

        String eventType = paymentEvent.getEventType();
        Long orderId = paymentEvent.getOrderId();
        // Long paymentId = paymentEvent.getPaymentId();

        switch (eventType) {
            case "PAYMENT_SUCCESS":
                System.out.println("🍽️ Payment succesfull for : Order #" + orderId);
                // TODO: change payment status to paid 
                break;
            case "PAYMENT_FAILED":
                System.out.println("🚫 Order cancelled: " + orderId);
                // TODO: Update internal DB
                break;
            default:
                System.out.println("⚠️ Unknown event type: " + eventType);
        }
    }
}
