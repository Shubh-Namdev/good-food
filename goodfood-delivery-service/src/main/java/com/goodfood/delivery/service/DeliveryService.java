package com.goodfood.delivery.service;

import com.goodfood.delivery.entity.Delivery;
import com.goodfood.delivery.kafka.DeliveryEventProducer;
import com.goodfood.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryEventProducer eventPublisher;


    public String updateDeliveryStatus(Long deliveryId, Long orderId, String status) {
        Delivery deliveryDetails = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery details not exists"));

        eventPublisher.publishDeliveryStatus(deliveryId, orderId, status);

        deliveryDetails.setStatus(status);
        deliveryRepository.save(deliveryDetails);

        return "Delivery status '" + status + "' sent for Order #" + orderId;
    }
}
