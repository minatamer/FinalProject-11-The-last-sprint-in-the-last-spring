package com.example.WallApp.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "post")
public class Post {
    @Id
    private String id;
    private String userId;
    private String content;
    private List<String> likedBy = new ArrayList<>();
    private List<String> sharedBy = new ArrayList<>();
    private LocalDateTime createdAt;
}

