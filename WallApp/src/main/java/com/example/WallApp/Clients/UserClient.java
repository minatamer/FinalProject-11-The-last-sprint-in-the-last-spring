package com.example.WallApp.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {

//  AUTHENTICATION
    @GetMapping("/validate-token/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String token);

//  FRIENDS

    @GetMapping("{userId}/friends")
    public ResponseEntity<List<UUID>> getFriends(@PathVariable("userId") UUID userId, @RequestHeader("Authorization") String token);

    @PostMapping("{userId}/friend/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable("userId") UUID userId, @PathVariable("friendId") UUID friendId, @RequestHeader("Authorization") String token);

    @DeleteMapping("{userId}/unfriend/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable UUID userId, @PathVariable UUID friendId, @RequestHeader("Authorization") String token);

    @GetMapping("check/{userId}")
    ResponseEntity<Boolean> checkUser(@PathVariable("userId") UUID userId, @RequestHeader("Authorization") String token);

    @GetMapping("/{userId}/token")
    public ResponseEntity<Map<String, String>> getUserToken(@PathVariable UUID userId);


}