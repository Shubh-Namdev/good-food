package com.goodfood.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderRequest {
    @NotNull
    private String restaurantId;

    @NotEmpty
    private List<OrderItemDto> items;
}
