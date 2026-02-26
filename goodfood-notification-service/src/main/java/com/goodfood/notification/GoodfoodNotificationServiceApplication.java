package com.goodfood.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class GoodfoodNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodfoodNotificationServiceApplication.class, args);
	}

}
