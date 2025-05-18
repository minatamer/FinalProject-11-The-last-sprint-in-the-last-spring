package com.example.MessagesApp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "messages")
public abstract class Message {
    @Id
    private UUID id;
    private UUID chatId;
    private UUID senderId;
    private LocalDateTime timestamp;
    private MessageStatus status;

    public Message() {
    }

    public Message(UUID id, UUID chatId, UUID senderId, LocalDateTime timestamp, MessageStatus status) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Message(UUID chatId, UUID senderId, LocalDateTime timestamp, MessageStatus status) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.status = status;
    }

    public abstract String getContentPreview();

    public abstract String getType();

    public UUID getId() {
        return id;
    }

    public UUID getChatId() {
        return chatId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
