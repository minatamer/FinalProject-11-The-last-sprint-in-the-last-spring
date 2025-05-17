package com.example.SearchApp.Clients;

import com.example.SearchApp.model.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "wall-service", url = "http://localhost:8085/wallApp/posts")

public interface WallClient {
    @GetMapping("/all")
    public List<PostDTO> getAllPosts();

}
