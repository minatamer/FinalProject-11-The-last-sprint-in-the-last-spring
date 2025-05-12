package com.example.WallApp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;
import com.example.WallApp.model.Subject;
import com.example.WallApp.model.Observer;

@Data

@Document(collection = "post")
public class Post implements Subject {

    @Id
    private UUID id ;

    private UUID userId;

    private String textContent;    // For text content
    private String imageUrl;       // For image content (could be a URL or base64 string)

    private List<UUID> likedBy ;
    private List<UUID> sharedBy ;
    private LocalDateTime createdAt ;
    private List<Observer> observers = new ArrayList<>();

    public Post(){
        this.observers = new ArrayList<>();
    }
    private Post(PostBuilder builder) {
        this.id = UUID.randomUUID();
        this.userId = builder.userId;
        this.textContent = builder.textContent;
        this.imageUrl = builder.imageUrl;
        this.likedBy = new ArrayList<>();
        this.sharedBy = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(textContent, imageUrl);
        }
    }

    public void updateObserved(String newText, String newImageUrl) {
        this.textContent = newText;
        this.imageUrl = newImageUrl;
        observedChanged();
    }

    public void observedChanged() {
        notifyObservers();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
        notifyObservers();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyObservers();
    }

    public List<UUID> getLikedBy() {
        return likedBy;
    }

    public List<UUID> getSharedBy() {
        return sharedBy;
    }

    public void setLikedBy(List<UUID> likedBy) {
        this.likedBy = likedBy;
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

    public static class PostBuilder {
        private UUID id;
        private UUID userId;
        private String textContent;
        private String imageUrl;
        private List<UUID> likedBy = new ArrayList<>();
        private List<UUID> sharedBy = new ArrayList<>();
        private LocalDateTime createdAt = LocalDateTime.now();




        public PostBuilder Id(UUID id) {
            this.id = id;
            return this;
        }

        public PostBuilder UserId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public PostBuilder TextContent(String textContent) {
            this.textContent = textContent;
            return this;
        }

        public PostBuilder ImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public PostBuilder LikedBy(List<UUID> likedBy) {
            this.likedBy = likedBy;
            return this;
        }


        public PostBuilder CreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }


        public Post build() {
            return new Post(this);
        }
    }
}


