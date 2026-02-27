package com.goodfood.notification.repository;

import com.goodfood.notification.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    Optional<NotificationLog> findByOrderIdAndEventType(Long orderId, String eventType);

    boolean existsByOrderIdAndEventType(Long orderId, String eventType);
}