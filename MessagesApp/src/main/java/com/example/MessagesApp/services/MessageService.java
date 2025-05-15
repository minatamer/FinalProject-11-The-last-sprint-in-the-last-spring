package com.example.MessagesApp.services;
import com.example.MessagesApp.factory.MessageFactory;
import com.example.MessagesApp.models.*;
import com.example.MessagesApp.observer.MessageNotifier;
import com.example.MessagesApp.repositories.ChatRepository;
import com.example.MessagesApp.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageFactory messageFactory;
    private final MessageNotifier messageNotifier;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          ChatRepository chatRepository,
                          MessageFactory messageFactory,
                          MessageNotifier messageNotifier) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.messageFactory = messageFactory;
        this.messageNotifier = messageNotifier;
    }

    public Message sendMessage(String chatId, String senderId,String receiverId, String content, String messageType) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        if (!chat.getParticipantIds().contains(senderId)) {
            throw new RuntimeException("User is not a participant of this chat");
        }

        Message message = messageFactory.createMessage(messageType);
        message.setChatId(chatId);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);

        Message savedMessage = messageRepository.save(message);

        // Update chat's last updated time
        chat.setUpdatedAt(LocalDateTime.now());
        chatRepository.save(chat);

        // Notify observers
        messageNotifier.notifyObservers(savedMessage);

        return savedMessage;
    }

    public List<Message> getChatMessages(String chatId) {
        return messageRepository.findByChatIdOrderByTimestampAsc(chatId);
    }

    public void markMessagesAsSeen(String chatId, String userId) {
        List<Message> messages = messageRepository.findByChatIdAndStatus(chatId, MessageStatus.DELIVERED);
        messages.forEach(message -> {
            if (!message.getSenderId().equals(userId)) {
                message.setStatus(MessageStatus.SEEN);
                messageRepository.save(message);
                messageNotifier.notifyObservers(message);
            }
        });
    }

    public List<Chat> getUserChats(String userId) {
        return chatRepository.findByParticipantIdsContaining(userId);
    }

    public Message updateMessageStatus(String messageId, MessageStatus status) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setStatus(status);
        Message updatedMessage = messageRepository.save(message);
        messageNotifier.notifyObservers(updatedMessage);
        return updatedMessage;
    }
}
