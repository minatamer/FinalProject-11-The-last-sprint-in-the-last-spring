package com.example.MessagesApp.repositories;

import com.example.MessagesApp.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByParticipantIdsContaining(UUID userId);
    Optional<Chat> findByParticipantIdsAndGroupChat(List<UUID> participantIds, boolean groupChat);
}