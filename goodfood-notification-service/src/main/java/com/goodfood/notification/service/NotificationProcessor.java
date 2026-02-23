package com.goodfood.notification.service;

import com.goodfood.notification.dto.NotificationEvent;
import com.goodfood.notification.entity.NotificationLog;
import com.goodfood.notification.repository.NotificationLogRepository;
import com.goodfood.notification.service.email.EmailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProcessor {

    private final NotificationLogRepository notificationLogRepository;
    private final EmailSender emailSender;

    public void process(NotificationEvent event) {

        log.info("Processing notification event: {}", event);

        // ✅ Idempotency Check
        boolean alreadyProcessed =
                notificationLogRepository.existsByOrderIdAndEventType(
                        event.getOrderId(),
                        event.getEventType()
                );

        if (alreadyProcessed) {
            log.info(
                    "Duplicate event detected. Skipping notification for orderId={}, eventType={}",
                    event.getOrderId(),
                    event.getEventType()
            );
            return;
        }

        // ✅ Send Email FIRST
        emailSender.sendEmail(
                event.getEmail(),
                buildSubject(event),
                buildBody(event)
        );

        // ✅ Persist SUCCESS only after email success
        NotificationLog logEntry = NotificationLog.builder()
                .orderId(event.getOrderId())
                .eventType(event.getEventType())
                .email(event.getEmail())
                .status("SUCCESS")
                .retryCount(0)
                .build();

        notificationLogRepository.save(logEntry);

        log.info("Notification successfully processed for orderId={}",
                event.getOrderId());
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