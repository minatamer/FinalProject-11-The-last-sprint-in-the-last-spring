package com.example.MessagesApp.observer;

import com.example.MessagesApp.models.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageStatusObserver implements MessageObserver {
    @Override
    public void update(Message message) {
        System.out.println("Message status updated: " + message.getId() + " - " + message.getStatus());
        // Here you can add logic to notify users about message status changes
    }
}
