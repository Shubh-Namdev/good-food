package com.goodfood.notification.service;

import com.goodfood.notification.dto.NotificationEvent;
import com.goodfood.notification.entity.NotificationLog;
import com.goodfood.notification.entity.NotificationStatus;
import com.goodfood.notification.repository.NotificationLogRepository;
import com.goodfood.notification.service.email.EmailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProcessor {

    private final NotificationLogRepository notificationLogRepository;
    private final EmailSender emailSender;

    @Value("${notification.processing-timeout-minutes:5}")
    private long processingTimeoutMinutes;

    public void process(NotificationEvent event) {

        log.info("Processing notification event: {}", event);

        NotificationLog logEntry =
                notificationLogRepository
                        .findByOrderIdAndEventType(
                                event.getOrderId(),
                                event.getEventType()
                        )
                        .orElse(null);

        
        if (logEntry != null) {

                if (logEntry.getStatus() == NotificationStatus.SUCCESS) {
                        log.info("Already processed. Skipping {}", event);
                        return;
                }

                if (logEntry.getStatus() == NotificationStatus.PROCESSING) {

                        if (!isProcessingTimedOut(logEntry)) {
                                log.info("Event currently being processed by another instance.");
                                return;
                        }

                        log.warn("Reclaiming stuck PROCESSING event {}", event);
                }
        }

        // ✅ Create or reuse record
        if (logEntry == null) {
                logEntry = NotificationLog.builder()
                        .orderId(event.getOrderId())
                        .eventType(event.getEventType())
                        .email(event.getEmail())
                        .retryCount(0)
                        .build();
        }

        logEntry.setStatus(NotificationStatus.PROCESSING);

        notificationLogRepository.save(logEntry);

        try {

            // ✅ External side effect
            emailSender.sendEmail(
                    event.getEmail(),
                    buildSubject(event),
                    buildBody(event)
            );

            // ✅ mark success
            logEntry.setStatus(NotificationStatus.SUCCESS);
            notificationLogRepository.save(logEntry);

            log.info("Notification SUCCESS for order {}",
                    event.getOrderId());

        } catch (Exception ex) {

            log.error("Notification FAILED", ex);

            logEntry.setStatus(NotificationStatus.FAILED);
            logEntry.setRetryCount(
                    logEntry.getRetryCount() + 1
            );

            notificationLogRepository.save(logEntry);

            // IMPORTANT → trigger Kafka retry
            throw ex;
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

    private boolean isProcessingTimedOut(NotificationLog log) {

        return log.getUpdatedAt()
                .plusMinutes(processingTimeoutMinutes)
                .isBefore(LocalDateTime.now());
        }

}