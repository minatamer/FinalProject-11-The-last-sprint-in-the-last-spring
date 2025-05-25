package com.example.MessagesApp.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendToUser(String messageContent, UUID userId) {
        if (messageContent == null) {
            // Handle null message content, either throw or just skip sending
            throw new IllegalArgumentException("Message content cannot be null");
            // Or:
            // System.err.println("Cannot send null message content for user: " + userId);
            // return;
        }
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.MESSAGE_ROUTING_KEY,
                messageContent
        );
        System.out.println("Message with content: " + messageContent + "is sent from user: " + userId);
    }
}

