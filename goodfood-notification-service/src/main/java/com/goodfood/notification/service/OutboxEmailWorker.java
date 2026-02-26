package com.goodfood.notification.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodfood.notification.dto.NotificationEvent;
import com.goodfood.notification.entity.OutboxEvent;
import com.goodfood.notification.entity.OutboxStatus;
import com.goodfood.notification.repository.OutboxEventRepository;
import com.goodfood.notification.service.email.EmailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEmailWorker {

    private final OutboxEventRepository repository;
    private final EmailSender emailSender;
    private final ObjectMapper mapper;

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() throws Exception {

        var events =
                repository.findTop50ByStatusOrderByCreatedAtAsc(
                        OutboxStatus.PENDING);

        for (OutboxEvent event : events) {

            try {

                NotificationEvent notification =
                        mapper.readValue(
                                event.getPayload(),
                                NotificationEvent.class);

                emailSender.sendEmail(
                        notification.getEmail(),
                        buildSubject(notification),
                        buildBody(notification)
                );

                event.setStatus(OutboxStatus.SENT);
                event.setProcessedAt(LocalDateTime.now());

            } catch (Exception ex) {

                event.setStatus(OutboxStatus.FAILED);
                log.error("Outbox processing failed", ex);
            }

            repository.save(event);
        }
    }

    private String buildSubject(NotificationEvent event) {
        return "GoodFood Order Update - " + event.getEventType();
    }

    private String buildBody(NotificationEvent event) {
        return """
                Hello,

                Your order update status is: %s
                Order ID: %d

                Thank you for using GoodFood!
                """
                .formatted(
                        event.getEventType(),
                        event.getOrderId()
                );
    }
}