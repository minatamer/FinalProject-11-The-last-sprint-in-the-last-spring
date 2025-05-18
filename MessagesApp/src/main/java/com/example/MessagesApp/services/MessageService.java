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
import java.util.Optional;
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

    public Message sendMessage(UUID chatId, UUID senderId, String content, String messageType) {
        System.out.println("ChatId: " + chatId);
        System.out.println("SenderId: " + senderId);
        System.out.println("Content: " + content);
        System.out.println("MessageType: " + messageType);

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        if(!Boolean.TRUE.equals(userClient.userExists(senderId).getBody())){
            throw new IllegalArgumentException("User with id " + senderId + " does not exist.");
        }

        if (!chat.getParticipantIds().contains(senderId)) {
            throw new RuntimeException("User is not a participant of this chat");
        }

        Message message = messageFactory.createMessage(messageType);
        message.setChatId(chatId);
        message.setSenderId(senderId);
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);

        if(message instanceof TextMessage) {
            ((TextMessage) message).setTextContent(content);
        } else if (message instanceof ImageMessage) {
            ((ImageMessage) message).setImageUrl(content);
            ((ImageMessage) message).setCaption(content.toLowerCase());
        } else if (message instanceof VideoMessage) {
            ((VideoMessage) message).setVideoUrl(content);
            ((VideoMessage) message).setSize(content.length());
            ((VideoMessage) message).setDuration(content.length());
        }

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
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        return messageRepository.findByChatIdOrderByTimestampAsc(chatId);
    }

    public List<Message> markMessagesAsSeen(UUID chatId, UUID userId) {
        Chat chat = chatRepository.findById(chatId)
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
        chatRepository.save(chat);
        return messageRepository.findByChatIdOrderByTimestampAsc(chatId);
    }

    public Message updateMessageStatus(UUID messageId, MessageStatus status) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setStatus(status);
        Message updatedMessage = messageRepository.save(message);
        messageNotifier.notifyObservers(updatedMessage);
        return updatedMessage;
    }

    @RabbitListener(queues = RabbitMQConfig.MESSAGE_QUEUE)
    public void notifyUser(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));


        System.out.println("Received message with id: " + messageId + " from userId: " + message.getSenderId() + " to list of users: ");
    }

    public Optional<Message> deleteMessage(UUID messageId){
        if (!messageRepository.existsById(messageId)) {
            throw new IllegalArgumentException("Message with ID " + messageId + " does not exist");
        }
        Optional<Message> deletedMessage = messageRepository.findById(messageId);
        messageRepository.deleteById(messageId);
        return deletedMessage;
    }

    public Message editTextMessage(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message with ID " + messageId + " does not exist"));

        // Check if it's a TextMessage and update content
        if (message instanceof TextMessage textMessage) {
            textMessage.setTextContent(newContent);
            return messageRepository.save(textMessage);
        } else {
            throw new IllegalArgumentException("Message with ID " + messageId + " is not a text message");
        }
    }
}
