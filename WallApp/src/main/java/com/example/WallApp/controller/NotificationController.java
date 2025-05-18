package com.example.WallApp.controller;

import com.example.WallApp.Clients.UserClient;
import com.example.WallApp.model.Notification;
import com.example.WallApp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/wallApp/notifications")
public class NotificationController {

    @Autowired
    private final NotificationService notificationService;

    @Autowired
    UserClient userClient;

    private final boolean authenticationEnabled = true;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    private boolean isAuthenticated(String token) {
        if (!authenticationEnabled) return true;

        if (token == null || token.isBlank()) return false;

        ResponseEntity<?> response = userClient.validateToken(token);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof Map<?, ?> body) {
            Object validObj = body.get("valid");
            return validObj instanceof Boolean && (Boolean) validObj;
        }

        return false;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable UUID userId) {
        List<Notification> notifications = notificationService.getNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
}
