package com.example.SearchApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "search")
public class Search {

    @Id
    private String id;
    private String searchInput;
    private UUID userId;

    public Search() {
        this.id = UUID.randomUUID().toString();
    }

    public Search(String searchInput, UUID userId) {
        this.id = UUID.randomUUID().toString();
        this.searchInput = searchInput;
        this.userId = userId;
    }
    // Getters and Setters

    public String getId() {
        return id;
    }

    public String getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
