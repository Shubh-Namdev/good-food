package com.goodfood.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodfood.notification.dto.NotificationEvent;
import com.goodfood.notification.entity.NotificationLog;
import com.goodfood.notification.entity.NotificationStatus;
import com.goodfood.notification.entity.OutboxEvent;
import com.goodfood.notification.entity.OutboxStatus;
import com.goodfood.notification.repository.NotificationLogRepository;
import com.goodfood.notification.repository.OutboxEventRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProcessor {

    private final NotificationLogRepository notificationLogRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public void process(NotificationEvent event) {

        try {
                boolean exists = notificationLogRepository
                        .existsByOrderIdAndEventType(event.getOrderId(), event.getEventType());

                if (exists) {
                        log.info("Duplicate event detected. Skipping orderId={}, eventType={}",
                                event.getOrderId(), event.getEventType());
                        return;
                }
                
                NotificationLog logEntry = NotificationLog.builder()
                .orderId(event.getOrderId())
                .eventType(event.getEventType())
                .email(event.getEmail())
                .status(NotificationStatus.PROCESSING)
                .retryCount(0)
                .build();

                notificationLogRepository.save(logEntry);

                OutboxEvent outbox = OutboxEvent.builder()
                        .aggregateType("NOTIFICATION")
                        .eventType(event.getEventType())
                        .payload(objectMapper.writeValueAsString(event))
                        .status(OutboxStatus.PENDING)
                        .build();

                outboxEventRepository.save(outbox);


                logEntry.setStatus(NotificationStatus.SUCCESS);
                notificationLogRepository.save(logEntry);
        }catch (Exception ex) {
                throw new RuntimeException("Notification processing failed", ex);
        }
    }
}