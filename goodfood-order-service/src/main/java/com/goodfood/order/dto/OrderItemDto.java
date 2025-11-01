package com.goodfood.order.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItemDto {
    private String menuItemId;
    private Integer quantity;
    private BigDecimal price;
}
