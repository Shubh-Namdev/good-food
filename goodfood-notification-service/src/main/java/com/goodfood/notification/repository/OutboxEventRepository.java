package com.goodfood.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goodfood.notification.entity.OutboxEvent;
import com.goodfood.notification.entity.OutboxStatus;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findTop50ByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
