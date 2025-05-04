package com.example.MessagesApp.repositories;

import com.example.MessagesApp.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByParticipantIdsContaining(String userId);
}
