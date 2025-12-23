package com.goodfood.payment.service;

import org.springframework.stereotype.Service;

import com.goodfood.payment.entity.Payment;
import com.goodfood.payment.entity.PaymentStatus;
import com.goodfood.payment.event.PaymentEvent;
import com.goodfood.payment.kafka.PaymentEventProducer;
import com.goodfood.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentProcessor {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer producer;

    public void process(Payment paymentDetails) {

        Payment paymentToProcess = paymentRepository.findById(paymentDetails.getId())
                .orElseThrow(() -> new RuntimeException("Payment not found for order " + paymentDetails.getOrderId()));

        //third party payment integration logic
        // boolean thirdPartyResponse = thirdPartyPaymentGateway(paymentDetails.getOrderId(), paymentDetails.getAmount());

        paymentToProcess.setStatus(PaymentStatus.PAYMENT_SUCCESS);
        Payment saved = paymentRepository.save(paymentToProcess);

        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setEventType(saved.getStatus().toString());
        paymentEvent.setOrderId(saved.getOrderId());
        paymentEvent.setPaymentId(saved.getId());
        paymentEvent.setAmount(saved.getAmount());

        producer.sendPaymentEvent(paymentEvent);

    }

    // private boolean thirdPartyPaymentGateway(Long Id, BigDecimal amount) {
    //     return true;
    // }
}



