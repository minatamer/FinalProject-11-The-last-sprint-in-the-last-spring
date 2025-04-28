package com.example.MessagesApp.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Hello from MessagesApp!";
    }
}
