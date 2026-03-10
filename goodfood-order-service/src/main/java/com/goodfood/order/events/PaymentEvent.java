package com.goodfood.order.events;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private String eventType;
    private Long orderId;
    private Long paymentId;
    private BigDecimal amount;
}
