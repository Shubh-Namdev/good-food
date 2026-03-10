package com.goodfood.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goodfood.payment.dtos.PaymentConfirmationRequest;
import com.goodfood.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<String> confirmPayment(
            @PathVariable Long orderId,
            @RequestBody PaymentConfirmationRequest request) {

        paymentService.confirmPayment(orderId, request.getStatus());

        return ResponseEntity.ok("Payment " + request.getStatus() + " for order " + orderId);
    }
}

