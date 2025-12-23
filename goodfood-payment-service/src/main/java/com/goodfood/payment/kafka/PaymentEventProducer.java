package com.goodfood.payment.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.goodfood.payment.event.PaymentEvent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void sendPaymentEvent(PaymentEvent event) {
        kafkaTemplate.send("payment-events", event);
        System.out.println("✅ Payment response sent");
    }
}

