package com.goodfood.order.controller;

import com.goodfood.order.dto.OrderRequest;
import com.goodfood.order.entity.Order;
import com.goodfood.order.service.OrderService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.getOrder(id));
    }

    @PostMapping
    public Order createOrder(@AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid OrderRequest request) {
        String customerId = jwt.getSubject();
        return orderService.createOrder(customerId, request);
    }
}
