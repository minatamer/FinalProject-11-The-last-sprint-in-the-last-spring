package com.example.WallApp.controller;

import com.example.WallApp.Clients.UserClient;
import com.example.WallApp.model.Notification;
import com.example.WallApp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/wallApp/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserClient userClient;

    private final boolean authenticationEnabled = true;

    @Autowired
    public NotificationController(NotificationService notificationService, UserClient userClient) {
        this.notificationService = notificationService;
        this.userClient = userClient;
    }

    private boolean isAuthenticated(String token, UUID userId) {
        if (!authenticationEnabled) return true;
        if (token == null || token.isBlank()) return false;

        ResponseEntity<Map<String, String>> tokenResponse = userClient.getUserToken(userId);
        if (tokenResponse.getStatusCode().is2xxSuccessful() && tokenResponse.getBody() != null) {
            String tokenId = tokenResponse.getBody().get("token");

            ResponseEntity<?> validationResponse = userClient.validateToken(token);

            return validationResponse.getStatusCode().is2xxSuccessful() && token.equals(tokenId);
        }

        return false;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotifications(@PathVariable UUID userId,
                                              @RequestHeader(value = "Authorization", required = false) String token) {

        if (!isAuthenticated(token, userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        List<Notification> notifications = notificationService.getNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
}
