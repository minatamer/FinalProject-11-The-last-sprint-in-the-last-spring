package com.example.UserApp.config;

import com.example.UserApp.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {
    private final UserService userService;

    @Autowired
    public RabbitMQListener(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = RabbitMQConfig.MESSAGE_QUEUE)
    public void receiveMessage(String messageContent) {
        System.out.println("UserApp received message: " + messageContent);
        userService.processIncomingMessage(messageContent);
    }
}
