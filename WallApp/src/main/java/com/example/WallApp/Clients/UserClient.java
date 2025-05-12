package com.example.WallApp.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8080/user")
public interface UserClient {

//    MY POSTS
 @GetMapping("/{userId}/myposts")
    public ResponseEntity<?> getMyPosts(@PathVariable("userId") UUID userId);

 @PostMapping(value="{userId}/myposts/{postId}", produces = "application/json")
    public ResponseEntity<String> addPost(@PathVariable("userId") UUID userId, @PathVariable("postId") UUID postId);

//  SHARED POSTS

 @GetMapping("{userId}/sharedposts")
    public ResponseEntity<?> getSharedPosts(@PathVariable("userId") UUID userId);

 @PostMapping("{userId}/sharedposts/{postId}")
    public ResponseEntity<?> setSharedPost(@PathVariable("userId") UUID userId, @PathVariable("postId") UUID postId);


 @GetMapping("check/{userId}")
 ResponseEntity<Boolean> checkUser(@PathVariable("userId") UUID userId);
}
