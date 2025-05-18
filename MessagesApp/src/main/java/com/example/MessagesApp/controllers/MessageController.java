package com.example.MessagesApp.controllers;
import com.example.MessagesApp.clients.UserClient;
import com.example.MessagesApp.models.Message;
import com.example.MessagesApp.models.*;
import com.example.MessagesApp.services.ChatService;
import com.example.MessagesApp.services.DatabasePopulationService;
import com.example.MessagesApp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {
    MessageService messageService;
    ChatService chatService;
    DatabasePopulationService databasePopulationService;

    @Autowired
    UserClient userClient;
    private final boolean authenticationEnabled = true;

    @Autowired
    public MessageController(MessageService messageService, ChatService chatService, DatabasePopulationService databasePopulationService) {
        this.messageService = messageService;
        this.chatService = chatService;
        this.databasePopulationService = databasePopulationService;
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

    @PostMapping("/populateRandomMessages")
    public void populateRandomMessages(@RequestHeader(value = "Authorization", required = false) String token) {
        databasePopulationService.populateMessages();
    }

    @PostMapping("/populateRandomChats")
    public void populateRandomChats(@RequestHeader(value = "Authorization", required = false) String token) {
        databasePopulationService.populateChats();
    }

    @PostMapping("/createDummyData")
    public void createDummyData(@RequestHeader(value = "Authorization", required = false) String token) {
        databasePopulationService.createDummyData();
    }

    @GetMapping("/getAllMessages")
    public List<Message> getAllMessages(@RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token.");
        }
        return messageService.getAllMessages();
    }

    @GetMapping("/getAllChats")
    public List<Chat> getAllChats(@RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token.");
        }
        return chatService.getAllChats();
    }

    @PostMapping("/chat/createChat")
    public ResponseEntity<?> createChat(@RequestBody Chat body, @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");

        }
        return ResponseEntity.ok(chatService.createChat(body.getName(), body.getParticipantIds()));
    }

    @PutMapping("/chat/addUser/{chatId}/{userId}")
    public ResponseEntity<?> addParticipant(@PathVariable UUID chatId,
                                            @PathVariable UUID userId,
                                            @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");

        }
        return ResponseEntity.ok(chatService.addParticipant(chatId, userId));
    }

    @PutMapping("/chat/removeParticipant/{chatId}/{userId}")
    public ResponseEntity<?> removeParticipant(
            @PathVariable UUID chatId,
            @PathVariable UUID userId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        Chat updatedChat = chatService.removeParticipant(chatId, userId);
        return ResponseEntity.ok(updatedChat);
    }

    @GetMapping("/chat/user/{userId}")
    public ResponseEntity<?> getUserChats(@PathVariable UUID userId, @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return ResponseEntity.ok(chatService.getUserChats(userId));
    }

    @PostMapping("/send/{chatId}/{senderId}/{type}")
    public ResponseEntity<?> sendMessage(@PathVariable UUID chatId,
                               @PathVariable UUID senderId,
                               @RequestBody Map<String, Object> content,
                               @PathVariable String type,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return ResponseEntity.ok(messageService.sendMessage(
                chatId,
                senderId,
                (String) content.get("content"),
                type));
    }

    @GetMapping("/chat/openChat/{chatId}")
    public ResponseEntity<?> getChatMessages(@PathVariable UUID chatId, @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return ResponseEntity.ok(messageService.getChatMessages(chatId));
    }

    @PutMapping("/seeChat/{chatId}/{userId}")
    public ResponseEntity<?> markMessagesAsSeen(@PathVariable UUID chatId,
                                                @PathVariable UUID userId,
                                                @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return ResponseEntity.ok(messageService.markMessagesAsSeen(chatId, userId));
    }

    @PutMapping("/chat/pin/{chatId}")
    public ResponseEntity<?> pinChat(@PathVariable UUID chatId, @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return ResponseEntity.ok(chatService.pinChat(chatId));
    }

    @PutMapping("/chat/unpin/{chatId}")
    public ResponseEntity<?> unpinChat(@PathVariable UUID chatId, @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return ResponseEntity.ok(chatService.unpinChat(chatId));
    }

    @PutMapping("/changeStatus/{messageId}/{status}")
    public ResponseEntity<?> updateMessageStatus(
            @PathVariable UUID messageId,
            @PathVariable MessageStatus status, @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return ResponseEntity.ok(messageService.updateMessageStatus(messageId, status));
    }

    @DeleteMapping("/deleteChat/{chatId}")
    public ResponseEntity<?> deleteChat(@PathVariable UUID chatId, @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return ResponseEntity.ok(chatService.deleteChat(chatId));
    }

    @DeleteMapping("/deleteMessage/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable UUID messageId, @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return ResponseEntity.ok(messageService.deleteMessage(messageId));
    }

    @PutMapping("/chat/updateName/{chatId}")
    public ResponseEntity<?> updateChat(
            @PathVariable UUID chatId,
            @RequestBody Map<String, Object> message, @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return ResponseEntity.ok(chatService.updateChat(chatId, (String) message.get("newName")));
    }

    @PutMapping("/editMessage/{messageId}")
    public ResponseEntity<?> editTextMessage(
            @PathVariable UUID messageId,
            @RequestBody Map<String, Object> message, @RequestHeader(value = "Authorization", required = false) String token) {
        if(!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }
        return ResponseEntity.ok(messageService.editTextMessage(messageId, (String) message.get("newContent")));
    }
}
