package com.goodfood.payment.kafka;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.goodfood.payment.entity.Payment;
import com.goodfood.payment.entity.PaymentStatus;
import com.goodfood.payment.event.OrderCreatedEvent;
import com.goodfood.payment.repository.PaymentRepository;
import com.goodfood.payment.service.PaymentProcessor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderEventListener {

    private final PaymentRepository paymentRepository;
    private final PaymentProcessor paymentProcessor;

    @KafkaListener(topics = "order-events", groupId = "payment-service-group")
    public void handleOrderEvent(OrderCreatedEvent event) {

        String eventType = event.getEventType();

        if (!"ORDER_CREATED".equals(eventType)) {
            return;
        }

        Long orderId = event.getOrderId();
        String customerId = event.getCustomerId();

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setCustomerId(customerId);
        payment.setAmount(event.getAmount());
        payment.setStatus(PaymentStatus.PAYMENT_PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        Payment paymentDetails = paymentRepository.save(payment);

        System.out.println("💳 Payment created in PENDING state for order: " + orderId);

        paymentProcessor.process(paymentDetails);
        
    }
}
