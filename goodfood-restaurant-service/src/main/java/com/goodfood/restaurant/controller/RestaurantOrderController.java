package com.goodfood.restaurant.controller;

import com.goodfood.restaurant.kafka.RestaurantEventProducer;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants/orders")
@RequiredArgsConstructor
public class RestaurantOrderController {

    @Autowired
    private final RestaurantEventProducer eventProducer;

    @PostMapping("/{orderId}/accept")
    public String acceptOrder(@PathVariable Long orderId, @RequestParam String restaurantId) {
        // You may update internal restaurant DB here too.
        eventProducer.publishOrderStatus(orderId, restaurantId, "ORDER_ACCEPTED");
        return "Order accepted by restaurant " + restaurantId;
    }

    @PostMapping("/{orderId}/reject")
    public String rejectOrder(@PathVariable Long orderId, @RequestParam String restaurantId) {
        eventProducer.publishOrderStatus(orderId, restaurantId, "ORDER_REJECTED");
        return "Order rejected by restaurant " + restaurantId;
    }

    @PostMapping("/{orderId}/ready")
    public String markReady(@PathVariable Long orderId, @RequestParam String restaurantId) {
        eventProducer.publishOrderStatus(orderId, restaurantId, "ORDER_READY");
        return "Order ready for pickup";
    }
}
