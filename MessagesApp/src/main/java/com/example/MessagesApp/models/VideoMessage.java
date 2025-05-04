package com.example.MessagesApp.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "messages")
public class VideoMessage extends Message {
    private String videoUrl;
    private int duration;



    @Override
    public String getType() {
        return "VIDEO";
    }
}
