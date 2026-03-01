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
                
                NotificationLog log = NotificationLog.builder()
                .orderId(event.getOrderId())
                .eventType(event.getEventType())
                .email(event.getEmail())
                .status(NotificationStatus.PROCESSING)
                .retryCount(0)
                .build();

                notificationLogRepository.save(log);

                OutboxEvent outbox = OutboxEvent.builder()
                        .aggregateType("NOTIFICATION")
                        .eventType(event.getEventType())
                        .payload(objectMapper.writeValueAsString(event))
                        .status(OutboxStatus.PENDING)
                        .build();

                outboxEventRepository.save(outbox);

                log.setStatus(NotificationStatus.SUCCESS);
                notificationLogRepository.save(log);
        }catch (Exception ex) {
                throw new RuntimeException("Notification processing failed", ex);
        }
    }




//      @Value("${notification.processing-timeout-minutes:5}")
//      private long processingTimeoutMinutes;
    
//     private boolean isProcessingTimedOut(NotificationLog log) {

//         return log.getUpdatedAt()
//                 .plusMinutes(processingTimeoutMinutes)
//                 .isBefore(LocalDateTime.now());
//         }

//     public void process(NotificationEvent event) {

//         log.info("Processing notification event: {}", event);

//         NotificationLog logEntry =
//                 notificationLogRepository
//                         .findByOrderIdAndEventType(
//                                 event.getOrderId(),
//                                 event.getEventType()
//                         )
//                         .orElse(null);

        
//         if (logEntry != null) {

//                 if (logEntry.getStatus() == NotificationStatus.SUCCESS) {
//                         log.info("Already processed. Skipping {}", event);
//                         return;
//                 }

//                 if (logEntry.getStatus() == NotificationStatus.PROCESSING) {

//                         if (!isProcessingTimedOut(logEntry)) {
//                                 log.info("Event currently being processed by another instance.");
//                                 return;
//                         }

//                         log.warn("Reclaiming stuck PROCESSING event {}", event);
//                 }
//         }

//         // ✅ Create or reuse record
//         if (logEntry == null) {
//                 logEntry = NotificationLog.builder()
//                         .orderId(event.getOrderId())
//                         .eventType(event.getEventType())
//                         .email(event.getEmail())
//                         .retryCount(0)
//                         .build();
//         }

//         logEntry.setStatus(NotificationStatus.PROCESSING);

//         notificationLogRepository.save(logEntry);

//         try {

//             // ✅ External side effect
//             emailSender.sendEmail(
//                     event.getEmail(),
//                     buildSubject(event),
//                     buildBody(event)
//             );

//             // ✅ mark success
//             logEntry.setStatus(NotificationStatus.SUCCESS);
//             notificationLogRepository.save(logEntry);

//             log.info("Notification SUCCESS for order {}",
//                     event.getOrderId());

//         } catch (Exception ex) {

//             log.error("Notification FAILED", ex);

//             logEntry.setStatus(NotificationStatus.FAILED);
//             logEntry.setRetryCount(
//                     logEntry.getRetryCount() + 1
//             );

//             notificationLogRepository.save(logEntry);

//             // IMPORTANT → trigger Kafka retry
//             throw ex;
//         }
//     }


}