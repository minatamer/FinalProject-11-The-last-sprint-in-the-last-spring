package com.example.MessagesApp.controllers;
import com.example.MessagesApp.models.Message;
import com.example.MessagesApp.models.*;
import com.example.MessagesApp.services.ChatService;
import com.example.MessagesApp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;
    private final ChatService chatService;

    @Autowired
    public MessageController(MessageService messageService, ChatService chatService) {
        this.messageService = messageService;
        this.chatService = chatService;
    }

    @PostMapping("/chat/createChat")
    public Chat createChat(@RequestParam String name, @RequestBody List<String> participantIds) {
        return chatService.createChat(participantIds, name);
    }

    @PostMapping("/chat/addUser")
    public Chat addParticipant(@RequestBody String chatId, @RequestBody String userId) {
        return chatService.addParticipant(chatId, userId);
    }

    @GetMapping("/chat/user/{userId}")
    public List<Chat> getUserChats(@PathVariable String userId) {
        return messageService.getUserChats(userId);
    }

    @PostMapping("/send")
    public Message sendMessage(
            @RequestParam String chatId,
            @RequestParam String senderId,
            @RequestParam String receiverId,
            @RequestParam String content,
            @RequestParam String messageType) {
        return messageService.sendMessage(chatId, senderId, receiverId, content, messageType);
    }

    @GetMapping("/chat/{chatId}")
    public List<Message> getChatMessages(@PathVariable String chatId) {
        return messageService.getChatMessages(chatId);
    }

    @PutMapping("/seen/{chatId}/{userId}")
    public void markMessagesAsSeen(@PathVariable String chatId, @PathVariable String userId) {
        messageService.markMessagesAsSeen(chatId, userId);
    }

    @PutMapping("/chat/pin/{chatId}")
    public void pinChat(@PathVariable String chatId) {
        chatService.pinChat(chatId);
    }

    @PutMapping("/chat/unpin/{chatId}")
    public void unpinChat(@PathVariable String chatId) {
        chatService.unpinChat(chatId);
    }

    @PutMapping("/status/{messageId}")
    public Message updateMessageStatus(
            @PathVariable String messageId,
            @RequestParam MessageStatus status) {
        return messageService.updateMessageStatus(messageId, status);
    }
}
