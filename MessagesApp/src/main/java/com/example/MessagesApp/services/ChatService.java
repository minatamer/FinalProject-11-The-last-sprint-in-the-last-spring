package com.example.MessagesApp.services;

import com.example.MessagesApp.clients.UserClient;
import com.example.MessagesApp.models.Chat;
import com.example.MessagesApp.repositories.ChatRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatService {
    ChatRepository chatRepository;
    UserClient userClient;

    @Autowired
    public ChatService(ChatRepository chatRepository, UserClient userClient) {
        this.chatRepository = chatRepository;
        this.userClient = userClient;
    }

    // Create a new chat (single or group)
    public Chat createChat(String chatName, List<UUID> participantIds) {
        // Validate participants exist
        participantIds.forEach(userId -> {
            if(!Boolean.TRUE.equals(userClient.userExists(userId).getBody())){
                throw new IllegalArgumentException("User with id " + userId + " does not exist.");
            }
        });

        // Prevent duplicate 1:1 chats
        if (participantIds.size() == 2) {
            chatRepository.findByParticipantIdsAndGroupChat(participantIds, false)
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
    public Chat addParticipant(UUID chatId, UUID userId){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        if (!chat.isGroupChat()) {
            throw new IllegalArgumentException("Only group chats can have added participants");
        }

        if(!Boolean.TRUE.equals(userClient.userExists(userId).getBody())){
            throw new IllegalArgumentException("User with id " + userId + " does not exist.");
        }

        if (chat.getParticipantIds() != null && chat.getParticipantIds().contains(userId)) {
            throw new IllegalArgumentException("User already in chat");
        }

        if(chat.getParticipantIds() == null){
            chat.setParticipantIds(List.of(userId));
        }
        else{
            chat.getParticipantIds().add(userId);
        }
        return chatRepository.save(chat);
    }

    public Chat removeParticipant(UUID chatId, UUID userIdToRemove) {
        // Find the chat or throw an exception if it doesn't exist
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found with ID: " + chatId));

        List<UUID> participants = chat.getParticipantIds();
        if (!participants.contains(userIdToRemove)) {
            throw new IllegalArgumentException("User " + userIdToRemove + " is not in this chat");
        }

        participants.remove(userIdToRemove);
        chat.setParticipantIds(participants);
        return chatRepository.save(chat);
    }

    public List<Chat> getUserChats(UUID userId) {
        if(!Boolean.TRUE.equals(userClient.userExists(userId).getBody())){
            throw new IllegalArgumentException("User with id " + userId + " does not exist.");
        }

        return chatRepository.findByParticipantIdsContaining(userId);
    }

    public Chat pinChat(UUID chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        if (chat.isPinned()) {
            throw new IllegalArgumentException("Chat is already pinned");
        }

        chat.setPinned(true);
        return chatRepository.save(chat);
    }

    public Chat unpinChat(UUID chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        if (!chat.isPinned()) {
            throw new IllegalArgumentException("Chat is already not pinned");
        }

        chat.setPinned(false);
        return chatRepository.save(chat);
    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public Optional<Chat> deleteChat(UUID chatId){
        if (!chatRepository.existsById(chatId)) {
            throw new IllegalArgumentException("Chat with ID " + chatId + " does not exist");
        }
        Optional<Chat> deletedChat = chatRepository.findById(chatId);
        chatRepository.deleteById(chatId);
        return deletedChat;
    }

    public Chat updateChat(UUID chatId, String newName) {
        return chatRepository.findById(chatId)
                .map(chat -> {
                    chat.setName(newName);
                    return chatRepository.save(chat);
                })
                .orElseThrow(() -> new IllegalArgumentException("Chat with ID " + chatId + " not found"));
    }




}
