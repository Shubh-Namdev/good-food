package com.goodfood.notification.consumer;

import com.goodfood.notification.dto.NotificationEvent;
import com.goodfood.notification.service.NotificationProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationProcessor notificationProcessor;

    @RetryableTopic(
        attempts = "3",
        backOff = @BackOff(delay = 2000, multiplier = 2.0),
        dltTopicSuffix = "-dlt"
    )
    @KafkaListener( topics = "order-events",groupId = "notification-service" )
    public void consumeOrderEvents(
            NotificationEvent event,
            Acknowledgment acknowledgment
    ) {
        processEvent(event, acknowledgment);
    }

    @KafkaListener( topics = "order-events-dlt", groupId = "notification-service")
    public void consumeOrderDlt(NotificationEvent event){
        log.error("Message moved to DLT: {}", event);
    }


    @RetryableTopic(
        attempts = "3",
        backOff = @BackOff(delay = 2000, multiplier = 2.0),
        dltTopicSuffix = "-dlt"
    )
    @KafkaListener( topics = "payment-events", groupId = "notification-service" )
    public void consumePaymentEvents(
            NotificationEvent event,
            Acknowledgment acknowledgment
    ) {
        processEvent(event, acknowledgment);
    }

    @KafkaListener( topics = "payment-events-dlt", groupId = "notification-service" )
    public void consumePaymentDlt(NotificationEvent event){
        log.error("Message moved to DLT: {}", event);
    }
    

    @RetryableTopic(
        attempts = "3",
        backOff = @BackOff(delay = 2000, multiplier = 2.0),
        dltTopicSuffix = "-dlt"
    )
    @KafkaListener( topics = "restaurant-events", groupId = "notification-service" )
    public void consumeRestaurantEvents(
            NotificationEvent event,
            Acknowledgment acknowledgment
    ) {
        processEvent(event, acknowledgment);
    }
    
    @KafkaListener( topics = "restaurant-events-dlt", groupId = "notification-service" )
    public void consumeRestaurantDlt(NotificationEvent event){
        log.error("Message moved to DLT: {}", event);
    }
    

    @RetryableTopic(
        attempts = "3",
        backOff = @BackOff(delay = 2000, multiplier = 2.0),
        dltTopicSuffix = "-dlt"
    )
    @KafkaListener( topics = "delivery-events", groupId = "notification-service" )
    public void consumeDeliveryEvents(
            NotificationEvent event,
            Acknowledgment acknowledgment
    ) {
        processEvent(event, acknowledgment);
    }

    @KafkaListener( topics = "delivery-events-dlt", groupId = "notification-service" )
    public void consumeDeliveryDlt(NotificationEvent event){
        log.error("Message moved to DLT: {}", event);
    }


    private void processEvent(
            NotificationEvent event,
            Acknowledgment acknowledgment
    ) {
        try {

            log.info("Received notification event: {}", event);

            notificationProcessor.process(event);

            // commit offset ONLY after success
            acknowledgment.acknowledge();

        } catch (Exception ex) {

            log.error("Failed processing event {}", event, ex);

            // DO NOT acknowledge
            // Kafka retry will happen automatically
            throw ex;
        }
    }
}