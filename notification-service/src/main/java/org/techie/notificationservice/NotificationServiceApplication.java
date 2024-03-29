package org.techie.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableKafka
@Slf4j
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notification_topic", groupId = "notification_id")
    public void notificationListener(OrderPlacedEvent orderPlacedEvent) {
//    public void notificationListener(String ts) {
        //Send out an email notification or text message.
        log.info("Received the notification for Order - {}", orderPlacedEvent.getOrderNumber());
//        log.info("Received the notification for Order - {}", ts);
    }

//    @KafkaListener(topics = "test_topic", groupId = "testgroup")
//    public void testListener(String test) {
//        //Send out an email notification or text message.
//        log.info("Received the test msg - {}", test);
//    }
}