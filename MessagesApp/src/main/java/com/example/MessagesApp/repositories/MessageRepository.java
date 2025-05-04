package com.example.MessagesApp.repositories;
import com.example.MessagesApp.models.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.MessagesApp.models.Message;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByChatIdOrderByTimestampAsc(String chatId);
    List<Message> findByChatIdAndStatus(String chatId, MessageStatus status);
    List<Message> findByChatIdAndSenderId(String chatId, String senderId);
}
