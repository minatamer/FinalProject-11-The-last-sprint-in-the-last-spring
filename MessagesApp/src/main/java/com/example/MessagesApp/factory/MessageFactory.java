package com.example.MessagesApp.factory;
import com.example.MessagesApp.models.ImageMessage;
import com.example.MessagesApp.models.Message;
import com.example.MessagesApp.models.TextMessage;
import com.example.MessagesApp.models.VideoMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageFactory {
    public Message createMessage(String messageType) {
        return switch (messageType.toUpperCase()) {
            case "TEXT" -> new TextMessage();
            case "IMAGE" -> new ImageMessage();
            case "VIDEO" -> new VideoMessage();
            default -> throw new IllegalArgumentException("Unsupported message type: " + messageType);
        };
    }
}
