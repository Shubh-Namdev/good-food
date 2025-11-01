package com.goodfood.delivery.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic deliveryTopic() {
        return new NewTopic("delivery-events", 1, (short) 1);
    }
}
