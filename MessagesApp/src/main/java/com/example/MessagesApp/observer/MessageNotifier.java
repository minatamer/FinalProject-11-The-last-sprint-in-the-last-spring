package com.example.MessagesApp.observer;

import com.example.MessagesApp.models.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageNotifier{

    private List<MessageObserver> observers = new ArrayList<>();

    public void addObserver(MessageObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MessageObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Message message) {
        for (MessageObserver observer : observers) {
            observer.onMessageSent(message);
        }
    }
}
