package com.example.MessagesApp.controllers;
import com.example.MessagesApp.models.Message;
import com.example.MessagesApp.models.*;
import com.example.MessagesApp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/chats")
    public Chat createChat(@RequestParam String name, @RequestBody List<String> participantIds) {
        return messageService.createChat(name, participantIds);
    }

    @GetMapping("/chats/user/{userId}")
    public List<Chat> getUserChats(@PathVariable String userId) {
        return messageService.getUserChats(userId);
    }

    @PostMapping("/send")
    public Message sendMessage(
            @RequestParam String chatId,
            @RequestParam String senderId,
            @RequestParam String content,
            @RequestParam String messageType) {
        return messageService.sendMessage(chatId, senderId, content, messageType);
    }

    @GetMapping("/chat/{chatId}")
    public List<Message> getChatMessages(@PathVariable String chatId) {
        return messageService.getChatMessages(chatId);
    }

    @PutMapping("/seen/{chatId}/{userId}")
    public void markMessagesAsSeen(@PathVariable String chatId, @PathVariable String userId) {
        messageService.markMessagesAsSeen(chatId, userId);
    }

    @PutMapping("/status/{messageId}")
    public Message updateMessageStatus(
            @PathVariable String messageId,
            @RequestParam MessageStatus status) {
        return messageService.updateMessageStatus(messageId, status);
    }
}
