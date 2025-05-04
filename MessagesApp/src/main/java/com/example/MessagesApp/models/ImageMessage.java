package com.example.MessagesApp.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class ImageMessage extends Message {
    private String imageUrl;
    private String caption;

    public ImageMessage() {
    }

    public ImageMessage(String id, String chatId, String senderId, String content, LocalDateTime timestamp, MessageStatus status) {
        super(id, chatId, senderId, content, timestamp, status);
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
}
