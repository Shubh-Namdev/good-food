package com.goodfood.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.goodfood.notification.entity.OutboxEvent;
import com.goodfood.notification.entity.OutboxStatus;

import jakarta.transaction.Transactional;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findTop50ByStatusOrderByCreatedAtAsc(OutboxStatus status);

    @Transactional
    @Modifying
    @Query(""" 
    UPDATE OutboxEvent o 
    SET o.status = 'PROCESSING' 
    WHERE o.id = :id 
    AND o.status = 'PENDING' 
    """)
    int claimEvent(Long id);
}
