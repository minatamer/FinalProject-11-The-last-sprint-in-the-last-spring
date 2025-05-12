package com.example.WallApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "follows")
public class Follows {

    @Id
    private String id;

    private UUID followerId;
    private UUID followedId;

    public Follows() {}

    public Follows(UUID followerId, UUID followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public UUID getFollowerId() { return followerId; }
    public void setFollowerId(UUID followerId) { this.followerId = followerId; }

    public UUID getFollowedId() { return followedId; }
    public void setFollowedId(UUID followedId) { this.followedId = followedId; }
}
