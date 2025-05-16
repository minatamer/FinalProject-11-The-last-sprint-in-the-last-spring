package com.example.SearchApp.Clients;

import com.example.SearchApp.model.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "wall-service", url = "http://localhost:8085/wallApp/posts")

public interface WallClient {
    @GetMapping("/all")
    public List<PostDTO> getAllPosts();

}
