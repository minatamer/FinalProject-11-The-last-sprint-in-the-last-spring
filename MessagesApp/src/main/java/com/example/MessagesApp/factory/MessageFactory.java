package com.example.MessagesApp.factory;
import com.example.MessagesApp.models.ImageMessage;
import com.example.MessagesApp.models.Message;
import com.example.MessagesApp.models.TextMessage;
import com.example.MessagesApp.models.VideoMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageFactory {
    public Message createMessage(String type) {
        switch (type.toUpperCase()) {
            case "TEXT":
                return new TextMessage();
            case "IMAGE":
                return new ImageMessage();
            case "VIDEO":
                return new VideoMessage();
            default:
                throw new IllegalArgumentException("Unknown message type: " + type);
        }
    }
}
