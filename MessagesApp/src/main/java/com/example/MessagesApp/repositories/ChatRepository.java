package com.example.MessagesApp.repositories;

import com.example.MessagesApp.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByParticipantIdsContaining(String userId);
    Optional<Chat> findByParticipantIdsAndType(List<String> participantIds, boolean groupChat);
}
