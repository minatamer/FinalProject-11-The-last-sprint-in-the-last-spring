package com.example.SearchApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "search")
public class Search {

    @Id
    private String id;
    private String searchInput;

    public Search() {
        this.id = UUID.randomUUID().toString();
    }

    public Search(String searchInput) {
        this.id = UUID.randomUUID().toString();
        this.searchInput = searchInput;
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
}
