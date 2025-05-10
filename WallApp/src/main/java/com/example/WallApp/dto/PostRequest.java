package com.example.WallApp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PostRequest {
    private UUID userId;       // UUID as a string
    private String textContent;
    private String imageUrl;
}

