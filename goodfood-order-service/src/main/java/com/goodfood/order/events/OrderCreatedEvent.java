package com.goodfood.order.events;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private String eventType;
    private Long orderId;
    private String restaurantId;
    private String customerId;
    private BigDecimal amount;
}
