package com.example.WallApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "notifications")
public class Notification {
    @Id
    private UUID id;
    private UUID receiverId;
    private String message;

    public Notification(UUID receiverId, String message) {
        this.id = UUID.randomUUID();
        this.receiverId = receiverId;
        this.message = message;
    }

    public Notification() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}