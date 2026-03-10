package com.goodfood.payment.dtos;

import com.goodfood.payment.entity.PaymentStatus;

public class PaymentConfirmationRequest {

    private PaymentStatus status;

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}

