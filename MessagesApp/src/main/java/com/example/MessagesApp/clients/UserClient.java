package com.example.MessagesApp.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8080/user")
public interface UserClient {

    @GetMapping("check/{userId}")
    ResponseEntity<Boolean> userExists(@PathVariable("userId")UUID userId);
}