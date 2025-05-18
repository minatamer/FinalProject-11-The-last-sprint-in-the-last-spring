package com.example.MessagesApp.controllers;
import com.example.MessagesApp.models.Message;
import com.example.MessagesApp.models.*;
import com.example.MessagesApp.services.ChatService;
import com.example.MessagesApp.services.DatabasePopulationService;
import com.example.MessagesApp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public MessageController(MessageService messageService, ChatService chatService, DatabasePopulationService databasePopulationService) {
        this.messageService = messageService;
        this.chatService = chatService;
        this.databasePopulationService = databasePopulationService;
    }

    @PostMapping("/populateRandomMessages")
    public void populateRandomMessages() {
        databasePopulationService.populateMessages();
    }

    @PostMapping("/populateRandomChats")
    public void populateRandomChats() {
        databasePopulationService.populateChats();
    }

    @PostMapping("/createDummyData")
    public void createDummyData() {
        databasePopulationService.createDummyData();
    }

    @GetMapping("/getAllMessages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/getAllChats")
    public List<Chat> getAllChats() {
        return chatService.getAllChats();
    }

    @PostMapping("/chat/createChat")
    public Chat createChat(@RequestBody Chat body) {
        return chatService.createChat(body.getName(), body.getParticipantIds());
    }

    @PutMapping("/chat/addUser/{chatId}/{userId}")
    public Chat addParticipant(@PathVariable UUID chatId, @PathVariable UUID userId) {
        return chatService.addParticipant(chatId, userId);
    }

    @PutMapping("/chat/removeParticipant/{chatId}/{userId}")
    public ResponseEntity<Chat> removeParticipant(
            @PathVariable UUID chatId,
            @PathVariable UUID userId) {
        Chat updatedChat = chatService.removeParticipant(chatId, userId);
        return ResponseEntity.ok(updatedChat);
    }

    @GetMapping("/chat/user/{userId}")
    public List<Chat> getUserChats(@PathVariable UUID userId) {
        return chatService.getUserChats(userId);
    }

    @PostMapping("/send/{chatId}/{senderId}/{type}")
    public Message sendMessage(@PathVariable UUID chatId, @PathVariable UUID senderId, @RequestBody Map<String, Object> content, @PathVariable String type) {
        return messageService.sendMessage(
                chatId,
                senderId,
                (String) content.get("content"),
                type);
    }

    @GetMapping("/chat/openChat/{chatId}")
    public List<Message> getChatMessages(@PathVariable UUID chatId) {
        return messageService.getChatMessages(chatId);
    }

    @PutMapping("/seeChat/{chatId}/{userId}")
    public List<Message> markMessagesAsSeen(@PathVariable UUID chatId, @PathVariable UUID userId) {
        return messageService.markMessagesAsSeen(chatId, userId);
    }

    @PutMapping("/chat/pin/{chatId}")
    public Chat pinChat(@PathVariable UUID chatId) {
        return chatService.pinChat(chatId);
    }

    @PutMapping("/chat/unpin/{chatId}")
    public Chat unpinChat(@PathVariable UUID chatId) {
        return chatService.unpinChat(chatId);
    }

    @PutMapping("/changeStatus/{messageId}/{status}")
    public Message updateMessageStatus(
            @PathVariable UUID messageId,
            @PathVariable MessageStatus status) {
        return messageService.updateMessageStatus(messageId, status);
    }

    @DeleteMapping("/deleteChat/{chatId}")
    public Optional<Chat> deleteChat(@PathVariable UUID chatId) {
        return chatService.deleteChat(chatId);
    }

    @DeleteMapping("/deleteMessage/{messageId}")
    public Optional<Message> deleteMessage(@PathVariable UUID messageId) {
        return messageService.deleteMessage(messageId);
    }

    @PutMapping("/chat/updateName/{chatId}")
    public Chat updateChat(
            @PathVariable UUID chatId,
            @RequestBody Map<String, Object> message) {
        return chatService.updateChat(chatId, (String) message.get("newName"));
    }

    @PutMapping("/editMessage/{messageId}")
    public Message editTextMessage(
            @PathVariable UUID messageId,
            @RequestBody Map<String, Object> message) {
        return messageService.editTextMessage(messageId, (String) message.get("newContent"));
    }
}
