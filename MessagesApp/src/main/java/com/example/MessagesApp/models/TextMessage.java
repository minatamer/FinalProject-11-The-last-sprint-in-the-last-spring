package com.example.MessagesApp.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

// Concrete message types implementing Factory pattern
@Data
@Document(collection = "messages")
public class TextMessage extends Message {
    @Override
    public String getType() {
        return "TEXT";
    }

    private String textContent;

    @Override
    public String getContentPreview() {
        return textContent.length() > 30 ? textContent.substring(0, 30) + "..." : textContent;
    }
}
