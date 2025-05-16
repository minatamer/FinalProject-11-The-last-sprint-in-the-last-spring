package com.example.SearchApp.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PostDTO {

    //combine
    private UUID userId;

    //search
    private String textContent;

    //filter
    private List<UUID> likedBy ;
    private List<UUID> sharedBy ;
    private LocalDateTime createdAt ;


    public PostDTO() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public List<UUID> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<UUID> likedBy) {
        this.likedBy = likedBy;
    }

    public List<UUID> getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(List<UUID> sharedBy) {
        this.sharedBy = sharedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
