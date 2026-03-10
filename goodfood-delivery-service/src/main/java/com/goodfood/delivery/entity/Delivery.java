package com.goodfood.delivery.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private String deliveryAgentId;
    private String status;

    private LocalDateTime assignedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime updatedAt;
}
