package com.example.MessagesApp.services;
import com.example.MessagesApp.clients.UserClient;
import com.example.MessagesApp.factory.MessageFactory;
import com.example.MessagesApp.models.*;
import com.example.MessagesApp.config.RabbitMQConfig;
import com.example.MessagesApp.config.RabbitMQProducer;
import com.example.MessagesApp.observer.MessageNotifier;
import com.example.MessagesApp.repositories.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {
    MessageRepository messageRepository;
    ChatRepository chatRepository;
    MessageFactory messageFactory;
    MessageNotifier messageNotifier;
    RabbitMQProducer rabbitMQProducer;
    UserClient userClient;

    @Autowired
    public MessageService(MessageFactory messageFactory,
                          ChatRepository chatRepository,
                          MessageRepository messageRepository,
                          MessageNotifier messageNotifier,
                          RabbitMQProducer rabbitMQProducer, UserClient userClient) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.messageFactory = messageFactory;
        this.messageNotifier = messageNotifier;
        this.rabbitMQProducer = rabbitMQProducer;
        this.userClient = userClient;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message sendMessage(UUID chatId, UUID senderId, String receiverId, String content, String messageType) {
        Chat chat = chatRepository.findById(String.valueOf(chatId))
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        if(!Boolean.TRUE.equals(userClient.userExists(senderId).getBody())){
            throw new IllegalArgumentException("User with id " + senderId + " does not exist.");
        }

        if(!Boolean.TRUE.equals(userClient.userExists(UUID.fromString(receiverId)).getBody())){
            throw new IllegalArgumentException("User with id " + receiverId + " does not exist.");
        }

        if (!chat.getParticipantIds().contains(senderId)) {
            throw new RuntimeException("User is not a participant of this chat");
        }

        Message message = messageFactory.createMessage(messageType);
        message.setChatId(chatId);
        message.setSenderId(senderId);
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);

        Message savedMessage = messageRepository.save(message);

        // Update chat's last updated time
        chat.setUpdatedAt(LocalDateTime.now());
        chatRepository.save(chat);

        // Notify observers
        messageNotifier.notifyObservers(savedMessage);
        this.rabbitMQProducer.sendToUser(message.getId(), message.getSenderId());

        return savedMessage;
    }

    public List<Message> getChatMessages(UUID chatId) {
        Chat chat = chatRepository.findById(String.valueOf(chatId))
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        return messageRepository.findByChatIdOrderByTimestampAsc(chatId);
    }

    public void markMessagesAsSeen(UUID chatId, UUID userId) {
        Chat chat = chatRepository.findById(String.valueOf(chatId))
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        if(!Boolean.TRUE.equals(userClient.userExists(userId).getBody())){
            throw new IllegalArgumentException("User with id " + userId + " does not exist.");
        }

        List<Message> messages = messageRepository.findByChatIdAndStatus(chatId, MessageStatus.DELIVERED);
        messages.forEach(message -> {
            if (!message.getSenderId().equals(userId)) {
                message.setStatus(MessageStatus.SEEN);
                messageRepository.save(message);
                messageNotifier.notifyObservers(message);
            }
        });
    }

    public Message updateMessageStatus(String messageId, MessageStatus status) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setStatus(status);
        Message updatedMessage = messageRepository.save(message);
        messageNotifier.notifyObservers(updatedMessage);
        return updatedMessage;
    }

    @RabbitListener(queues = RabbitMQConfig.MESSAGE_QUEUE)
    public void notifyUser(String senderId, String receiverId, String messageId) {
        if(!Boolean.TRUE.equals(userClient.userExists(UUID.fromString(senderId)).getBody())){
            throw new IllegalArgumentException("User with id " + senderId + " does not exist.");
        }

        if(!Boolean.TRUE.equals(userClient.userExists(UUID.fromString(receiverId)).getBody())){
            throw new IllegalArgumentException("User with id " + receiverId + " does not exist.");
        }

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        System.out.println("Received message with id: " + messageId + " from userId: " + senderId + " to userId: " + receiverId);
    }
}
