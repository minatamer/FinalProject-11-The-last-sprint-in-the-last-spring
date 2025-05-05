package com.example.WallApp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "post")
public class Post {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private UUID userId;

    private String textContent;    // For text content
    private String imageUrl;       // For image content (could be a URL or base64 string)

    @Builder.Default
    private List<String> likedBy = new ArrayList<>();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

