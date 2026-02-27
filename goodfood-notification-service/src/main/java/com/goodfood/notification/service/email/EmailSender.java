package com.goodfood.notification.service.email;

public interface EmailSender {

    void sendEmail(
            String to,
            String subject,
            String body
    );
}