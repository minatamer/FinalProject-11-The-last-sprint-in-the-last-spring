package com.example.WallApp.controller;

import com.example.WallApp.Clients.UserClient;
import com.example.WallApp.dto.PostRequest;
import com.example.WallApp.model.Post;
import com.example.WallApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/wallApp/posts")
public class PostController {

    @Autowired
    private final PostService postService;

    @Autowired
    UserClient userClient;
    private final boolean authenticationEnabled = true;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    private boolean isAuthenticated(String token,UUID userId) {
        if (!authenticationEnabled) return true;

        if (token == null || token.isBlank()) return false;

        String tokenId = userClient.getUserToken(userId).getBody().get("token");
        ResponseEntity<?> response = userClient.validateToken(token);
        System.out.println(tokenId);
        System.out.println(token);
        System.out.println(token.equals(tokenId));
        if (response.getStatusCode().is2xxSuccessful() && tokenId.equals(token)) {
            return true;
        }


        return false;
    }
    private boolean isAuthenticated2(String token) {
        if (!authenticationEnabled) return true;

        if (token == null || token.isBlank()) return false;

        ResponseEntity<?> response = userClient.validateToken(token);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof Map<?, ?> body) {
            Object validObj = body.get("valid");
            return validObj instanceof Boolean && (Boolean) validObj;
        }

        return false;
    }



    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest,
                                        @RequestHeader(value = "Authorization", required = false) String token) {
        UUID userId = postRequest.getUserId();
        if(!isAuthenticated(token,userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");

        }
        return ResponseEntity.ok(postService.addPost(postRequest));
    }

    @PostMapping("/dummy")
    public ResponseEntity<?> populateDummyPosts( @RequestHeader(value = "Authorization", required = false) String token) {
        return postService.populateDummyPosts();
    }

    @GetMapping("/all")
    public List<Post> getAllPosts( @RequestHeader(value = "Authorization", required = false) String token ) {
//        if(!isAuthenticated(token)) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token.");
//        }
        return postService.getAllPosts();
    }

    @GetMapping("/{userId}/myposts")
    public ResponseEntity<?> getMyPosts(@PathVariable UUID userId,
                                        @RequestHeader(value = "Authorization", required = false) String token) {

        // Verify authentication if needed
        if (authenticationEnabled && !isAuthenticated2(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Login Token.");
        }

        // Fetch the posts
        List<Post> posts = postService.getMyPosts(userId);

        // Return the list of posts
        return ResponseEntity.ok(posts);
    }




// ES2AL FEEHA 3AL GROUP
    @GetMapping("/{id}")
    public Optional<Post> getPostById(@PathVariable UUID id, @RequestHeader(value = "Authorization", required = false) String token) {
        UUID userID = postService.getPostById(id).get().getUserId();
        if (!isAuthenticated(token,userID)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token.");
        }
        return postService.getPostById(id);
    }




    @PutMapping("/{id}")
    public Post updatePost(@PathVariable UUID id, @RequestBody PostRequest updatedPost, @RequestHeader(value = "Authorization", required = false) String token) {
        UUID userID = postService.getPostById(id).get().getUserId();
        if (!isAuthenticated(token,userID)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token.");
        }
        return postService.updatePost(id,updatedPost);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable UUID id,@RequestHeader(value = "Authorization", required = false) String token) {
        UUID userID = postService.getPostById(id).get().getUserId();
        if (!isAuthenticated(token,userID)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token.");
        }
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
    public Optional<Post> likePost(@PathVariable UUID id, @PathVariable UUID userId,@RequestHeader(value = "Authorization", required = false) String token) {
        UUID userID = postService.getPostById(id).get().getUserId();
        if (!isAuthenticated(token,userID)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token.");
        }
        Optional<Post> postOpt = postService.getPostById(id);
        return postOpt.flatMap(post -> postService.likePost(post, userId));
    }

    @PostMapping("/{userId}/sharepost/{id}")
    public ResponseEntity<?> sharePost(@PathVariable UUID userId,@PathVariable UUID id,@RequestHeader(value = "Authorization", required = false) String token) {
        UUID userID = postService.getPostById(id).get().getUserId();
        if(!isAuthenticated(token,userID)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return postService.sharePost(userId,id);
    }
    @PostMapping("/{userId}/friend/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable UUID userId,@PathVariable UUID friendId,@RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token,userId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        //
        return postService.addFriend(userId,friendId);
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<UUID>> getFriends(@PathVariable UUID userId,@RequestHeader(value = "Authorization", required = false) String token){
        if(!isAuthenticated(token,userId)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token.");
        }
        return postService.getFriends(userId);
    }

    @DeleteMapping("/{userId}/unfriend/{friendId}")
    public ResponseEntity<?> removeFriend(@PathVariable UUID userId, @PathVariable UUID friendId,@RequestHeader(value = "Authorization", required = false) String token){
        if (!isAuthenticated(token,userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return postService.removeFriend(userId,friendId);
}
}