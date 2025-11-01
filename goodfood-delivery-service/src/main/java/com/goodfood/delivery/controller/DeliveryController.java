package com.goodfood.delivery.controller;

import com.goodfood.delivery.entity.Delivery;
import com.goodfood.delivery.kafka.DeliveryEventProducer;
import com.goodfood.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryEventProducer eventProducer;

    @GetMapping
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Delivery getDelivery(@PathVariable Long id) {
        return deliveryRepository.findById(id).orElseThrow();
    }

    @PostMapping("/{orderId}/status")
    public String updateDeliveryStatus(
            @PathVariable Long orderId,
            @RequestParam Long deliveryId,
            @RequestParam String status) {
                
        eventProducer.publishDeliveryStatus(deliveryId, orderId, status);
        return "Delivery status '" + status + "' sent for Order #" + orderId;
    }

}
