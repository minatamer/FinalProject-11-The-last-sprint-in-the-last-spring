package com.example.WallApp.controller;

import com.example.WallApp.dto.PostRequest;
import com.example.WallApp.model.Post;
import com.example.WallApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/wallApp/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Post createPost(@RequestBody PostRequest postRequest) {
        return postService.addPost(postRequest);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable String id, @RequestBody Post updatedPost) {
        updatedPost.setId(UUID.fromString(id));
        return postService.updatePost(updatedPost);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable String id) {
        Post post = new Post();
        post.setId(UUID.fromString(id));
        postService.deletePost(post);
    }

    @PutMapping("/{id}/like")
    public Optional<Post> likePost(@PathVariable String id, @RequestParam String userId) {
        Optional<Post> postOpt = postService.getPostById(id);
        return postOpt.flatMap(post -> postService.likePost(post, userId));
    }

//    @PutMapping("/{id}/share")
//    public Optional<Post> sharePost(@PathVariable String id, @RequestParam String userId) {
//        Optional<Post> postOpt = postService.getPostById(id);
//        return postOpt.flatMap(post -> postService.sharePost(post, userId));
//    }
}

