package com.example.MessagesApp.observer;
import com.example.MessagesApp.models.Message;

public interface MessageObserver {
    void onMessageSent(Message message);
}


