package com.example.MessagesApp.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class VideoMessage extends Message {
    private String videoUrl;
    private int duration;
    private long size;

    @Override
    public String getType() {
        return "VIDEO";
    }

    public VideoMessage() {
    }

    public VideoMessage(String id, String chatId, String senderId, String receiverId, String content, LocalDateTime timestamp, MessageStatus status, String videoUrl, int duration, long size) {
        super(id, chatId, senderId, receiverId, content, timestamp, status);
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.size = size;
    }

    public VideoMessage(String chatId, String senderId, String receiverId, String content, LocalDateTime timestamp, MessageStatus status, String videoUrl, int duration, long size) {
        super(chatId, senderId, receiverId, content, timestamp, status);
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.size = size;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
