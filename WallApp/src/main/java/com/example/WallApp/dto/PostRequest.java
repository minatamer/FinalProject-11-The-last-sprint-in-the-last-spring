package com.example.WallApp.dto;

import java.util.UUID;

public class PostRequest {
    private UUID userId;
    private String textContent;
    private String imageUrl;

    // Constructor
    public PostRequest(UUID userId, String textContent, String imageUrl) {
        this.userId = userId;
        this.textContent = textContent;
        this.imageUrl = imageUrl;
    }

    // Default constructor (optional if using Jackson for deserialization)
    public PostRequest() {}

    // Getters
    public UUID getUserId() {
        return userId;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setters
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
