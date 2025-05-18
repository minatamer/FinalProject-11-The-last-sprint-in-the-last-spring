package com.example.MessagesApp.controllers;
import com.example.MessagesApp.models.Message;
import com.example.MessagesApp.models.*;
import com.example.MessagesApp.services.ChatService;
import com.example.MessagesApp.services.DatabasePopulationService;
import com.example.MessagesApp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {
    MessageService messageService;
    ChatService chatService;
    DatabasePopulationService databasePopulationService;

    @Autowired
    public MessageController(MessageService messageService, ChatService chatService) {
        this.messageService = messageService;
        this.chatService = chatService;
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

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @PostMapping("/chat/createChat")
    public Chat createChat(@RequestParam String name, @RequestBody List<UUID> participantIds) {
        return chatService.createChat(participantIds, name);
    }

    @PostMapping("/chat/addUser")
    public Chat addParticipant(@RequestBody UUID chatId, @RequestBody UUID userId) {
        return chatService.addParticipant(chatId, userId);
    }

    @GetMapping("/chat/user/{userId}")
    public List<Chat> getUserChats(@PathVariable UUID userId) {
        return chatService.getUserChats(userId);
    }

    @PostMapping("/send")
    public Message sendMessage(
            @RequestParam UUID chatId,
            @RequestParam UUID senderId,
            @RequestParam String content,
            @RequestParam String messageType) {
        return messageService.sendMessage(chatId, senderId, content, messageType);
    }

    @GetMapping("/chat/{chatId}")
    public List<Message> getChatMessages(@PathVariable UUID chatId) {
        return messageService.getChatMessages(chatId);
    }

    @PutMapping("/seen/{chatId}/{userId}")
    public void markMessagesAsSeen(@PathVariable UUID chatId, @PathVariable UUID userId) {
        messageService.markMessagesAsSeen(chatId, userId);
    }

    @PutMapping("/chat/pin/{chatId}")
    public void pinChat(@PathVariable UUID chatId) {
        chatService.pinChat(chatId);
    }

    @PutMapping("/chat/unpin/{chatId}")
    public void unpinChat(@PathVariable UUID chatId) {
        chatService.unpinChat(chatId);
    }

    @PutMapping("/status/{messageId}")
    public Message updateMessageStatus(
            @PathVariable UUID messageId,
            @RequestParam MessageStatus status) {
        return messageService.updateMessageStatus(messageId, status);
    }
}
