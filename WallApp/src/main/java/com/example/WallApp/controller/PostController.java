package com.example.WallApp.controller;

import com.example.WallApp.dto.PostRequest;
import com.example.WallApp.model.Post;
import com.example.WallApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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

    @GetMapping("/all")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
    @GetMapping("/{id}")
    public Optional<Post> getPostById(@PathVariable UUID id) {
        return postService.getPostById(id);
    }
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable UUID id, @RequestBody PostRequest updatedPost) {
        return postService.updatePost(id,updatedPost);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable UUID id) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            postService.deletePostById(id);
            System.out.println("Post " +id+ " deleted");
        }

    }
    @DeleteMapping("/deleteAll")
    public void deleteAllPosts() {
        postService.deleteAllPosts();
    }

    @PutMapping("/{id}/like")
    public Optional<Post> likePost(@PathVariable UUID id, @RequestParam String userId) {
        Optional<Post> postOpt = postService.getPostById(id);
        return postOpt.flatMap(post -> postService.likePost(post, userId));
    }

//    @PutMapping("/{id}/share")
//    public Optional<Post> sharePost(@PathVariable String id, @RequestParam String userId) {
//        Optional<Post> postOpt = postService.getPostById(id);
//        return postOpt.flatMap(post -> postService.sharePost(post, userId));
//    }
}

