package com.example.MessagesApp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "chats")
public class Chat {
    @Id
    private UUID id;
    private String name;
    private List<UUID> participantIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean pinned;
    private boolean groupChat;
    @DBRef
    private List<Message> messages;

    public Chat() {
    }

    public Chat(UUID id, String name, List<UUID> participantIds, LocalDateTime createdAt, LocalDateTime updatedAt, List<Message> messages, boolean pinned, boolean groupChat) {
        this.id = id;
        this.name = name;
        this.participantIds = participantIds;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messages = messages;
        this.pinned = pinned;
        this.groupChat = groupChat;
    }

    public Chat(String name, List<UUID> participantIds, LocalDateTime createdAt, LocalDateTime updatedAt, List<Message> messages, boolean pinned, boolean groupChat) {
        this.name = name;
        this.participantIds = participantIds;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messages = messages;
        this.pinned = pinned;
        this.groupChat = groupChat;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UUID> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<UUID> participantIds) {
        this.participantIds = participantIds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isGroupChat() {
        return groupChat;
    }

    public void setGroupChat(boolean groupChat) {
        this.groupChat = groupChat;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
