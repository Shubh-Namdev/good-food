package com.goodfood.payment.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.goodfood.payment.entity.Payment;
import com.goodfood.payment.entity.PaymentStatus;
import com.goodfood.payment.repository.PaymentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void confirmPayment(Long orderId, PaymentStatus newStatus) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            throw new AccessDeniedException("Only customers can confirm payments");
        }

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order " + orderId));

        if (payment.getStatus() != PaymentStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("Payment already processed");
        }

        payment.setStatus(newStatus);
        paymentRepository.save(payment);
    }
}

