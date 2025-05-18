package com.example.MessagesApp.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "messages")
public class VideoMessage extends Message {
    private String videoUrl;
    private long duration;
    private long size;

    @Override
    public String getType() {
        return "VIDEO";
    }

    public VideoMessage() {
    }

    @Override
    public String getContentPreview() {
        return "[Video] " + getVideoUrl();
    }

    public VideoMessage(UUID id, UUID chatId, UUID senderId, LocalDateTime timestamp, MessageStatus status, String videoUrl, long duration, long size) {
        super(id, chatId, senderId, timestamp, status);
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.size = size;
    }

    public VideoMessage(UUID chatId, UUID senderId, LocalDateTime timestamp, MessageStatus status, String videoUrl, long duration, long size) {
        super(chatId, senderId, timestamp, status);
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
