package com.example.MessagesApp.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
public class ImageMessage extends Message {
    private String imageUrl;
    private String caption;

    public ImageMessage() {
    }

    public ImageMessage(String id, String chatId, String senderId, String receiverId, String content, LocalDateTime timestamp, MessageStatus status, String imageUrl, String caption) {
        super(id, chatId, senderId, receiverId, content, timestamp, status);
        this.caption = caption;
        this.imageUrl = imageUrl;
    }

    public ImageMessage(String chatId, String senderId, String receiverId, String content, LocalDateTime timestamp, MessageStatus status, String imageUrl, String caption) {
        super(chatId, senderId, receiverId, content, timestamp, status);
        this.caption = caption;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getType() {
        return "IMAGE";
    }

    @Override
    public String getContentPreview() {
        return "[Image] " + getImageUrl();
    }
}
