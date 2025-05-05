package com.example.WallApp.dto;

import lombok.Data;

@Data
public class PostRequest {
    private String userId;       // UUID as a string
    private String textContent;
    private String imageUrl;
}

