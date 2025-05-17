package com.example.WallApp.controller;

import com.example.WallApp.dto.PostRequest;
import com.example.WallApp.model.Post;
import com.example.WallApp.repository.PostRepository;
import com.example.WallApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/wallApp/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.addPost(postRequest));
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

    @PutMapping("/{userId}/like/{id}")
    public Optional<Post> likePost(@PathVariable UUID id, @PathVariable UUID userId) {
        Optional<Post> postOpt = postService.getPostById(id);
        return postOpt.flatMap(post -> postService.likePost(post, userId));
    }

    @PostMapping("/{userId}/sharedposts/{id}")
    public ResponseEntity<?> sharePost(@PathVariable UUID userId,@PathVariable UUID id) {
        return postService.sharePost(userId,id);
    }
    @PostMapping("/{userId}/friend/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable UUID userId,@PathVariable UUID friendId) {
        return postService.addFriend(userId,friendId);
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<UUID>> getFriends(@PathVariable UUID userId){
        return postService.getFriends(userId);
    }

    @DeleteMapping("/{userId}/unfriend/{friendId}")
    public ResponseEntity<?> removeFriend(@PathVariable UUID userId, @PathVariable UUID friendId){
        return postService.removeFriend(userId,friendId);
}
}