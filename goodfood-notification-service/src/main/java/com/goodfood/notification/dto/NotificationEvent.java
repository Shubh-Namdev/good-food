package com.goodfood.notification.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {

    private String eventType;

    private Long orderId;

    private String email;

    // Optional fields depending on event
    private Long paymentId;
    private Long deliveryId;
    private String restaurantId;

    private BigDecimal amount;
}