package com.example.MessagesApp.services;

import com.example.MessagesApp.models.Chat;
import com.example.MessagesApp.observer.MessageNotifier;
import com.example.MessagesApp.repositories.ChatRepository;
import com.example.MessagesApp.repositories.MessageRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
//    private final UserServiceClient userServiceClient; // Feign client for user validation
    private final MessageNotifier messageNotifier; // For Observer pattern

    @Autowired
    public ChatService(ChatRepository chatRepository,
                       MessageRepository messageRepository,
//                       UserServiceClient userServiceClient,
                       MessageNotifier messageNotifier) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
//        this.userServiceClient = userServiceClient;
        this.messageNotifier = messageNotifier;
    }

    // Create a new chat (single or group)
    public Chat createChat(List<String> participantIds, String chatName) {
        // Validate participants exist
//        participantIds.forEach(userId -> {
//            if (!userServiceClient.userExists(userId)) {
//                throw new ResourceNotFoundException("User not found: " + userId);
//            }
//        });

        // Prevent duplicate 1:1 chats
        if (participantIds.size() == 2) {
            chatRepository.findByParticipantIdsAndType(participantIds, false)
                    .ifPresent(chat -> {
                        try {
                            throw new BadRequestException("Chat already exists");
                        } catch (BadRequestException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        Chat chat = new Chat();
        chat.setParticipantIds(participantIds);
        chat.setName(chatName);
        chat.setGroupChat(participantIds.size() > 2);
        chat.setCreatedAt(LocalDateTime.now());

        return chatRepository.save(chat);
    }

    // Add participant to group chat
    public Chat addParticipant(String chatId, String userId){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        if (!chat.isGroupChat()) {
            throw new IllegalArgumentException("Only group chats can have added participants");
        }

//        if (!userServiceClient.userExists(userId)) {
//            throw new ResourceNotFoundException("User not found");
//        }

        if (chat.getParticipantIds().contains(userId)) {
            throw new IllegalArgumentException("User already in chat");
        }

        chat.getParticipantIds().add(userId);
        return chatRepository.save(chat);
    }

    public List<Chat> getUserChats(String userId) {
        return chatRepository.findByParticipantIdsContaining(userId);
    }

    public Chat pinChat(String chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        if (chat.isPinned()) {
            throw new IllegalArgumentException("Chat is already pinned");
        }

        chat.setGroupChat(true);
        return chatRepository.save(chat);
    }

    public Chat unpinChat(String chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        if (!chat.isPinned()) {
            throw new IllegalArgumentException("Chat is already not pinned");
        }

        chat.setGroupChat(false);
        return chatRepository.save(chat);
    }
}
