package com.example.WallApp.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8080/user")
public interface UserClient {



//  FRIENDS

    @GetMapping("{userId}/friends")
    public ResponseEntity<List<UUID>> getFriends(@PathVariable("userId") UUID userId);

    @PostMapping("{userId}/friend/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable("userId") UUID userId, @PathVariable("friendId") UUID friendId);

    @DeleteMapping("{userId}/unfriend/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable UUID userId, @PathVariable UUID friendId);

    @GetMapping("check/{userId}")
    ResponseEntity<Boolean> checkUser(@PathVariable("userId")UUID userId);
}
