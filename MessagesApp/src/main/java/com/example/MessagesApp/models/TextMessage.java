package com.example.MessagesApp.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "messages")
public class TextMessage extends Message {

    public TextMessage() {
    }

    public TextMessage(UUID id, UUID chatId, UUID senderId, LocalDateTime timestamp, MessageStatus status, String textContent) {
        super(id, chatId, senderId, timestamp, status, textContent);
        this.textContent = textContent;
    }

    public TextMessage(UUID chatId, UUID senderId, LocalDateTime timestamp, MessageStatus status, String textContent) {
        super(chatId, senderId, timestamp, status, textContent);
        this.textContent = textContent;
    }

    @Override
    public String getType() {
        return "TEXT";
    }

    private String textContent;

    @Override
    public String getContentPreview() {
        return textContent.length() > 30 ? textContent.substring(0, 30) + "..." : textContent;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}
