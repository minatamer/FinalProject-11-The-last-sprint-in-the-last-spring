package com.example.MessagesApp.repositories;
import com.example.MessagesApp.models.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.MessagesApp.models.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByChatIdOrderByTimestampAsc(UUID chatId);
    List<Message> findByChatIdAndStatus(UUID chatId, MessageStatus status);
}
