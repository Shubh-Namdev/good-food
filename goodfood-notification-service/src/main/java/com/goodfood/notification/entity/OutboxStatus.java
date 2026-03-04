package com.goodfood.notification.entity;

public enum OutboxStatus {
    PENDING,
    PROCESSING,
    SENT,
    FAILED
}
