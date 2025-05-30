package com.example.MessagesApp.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {

    @GetMapping("/check/{userId}")
    ResponseEntity<Boolean> userExists(@PathVariable("userId")UUID userId);

    //  AUTHENTICATION
    @GetMapping("/validate-token/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String token);
}