package com.example.MessagesApp.services;

import com.example.MessagesApp.factory.MessageFactory;
import com.example.MessagesApp.models.*;
import com.example.MessagesApp.repositories.ChatRepository;
import com.example.MessagesApp.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class DatabasePopulationService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageFactory messageFactory;


    @Autowired
    public DatabasePopulationService(MessageRepository messageRepository, ChatRepository chatRepository, MessageFactory messageFactory) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.messageFactory = messageFactory;
    }

    @Transactional
    public void populateChats() {
        for (int i = 1; i <= 10; i++) {
            Chat chat = new Chat();
            chat.setName("Chat: " + i);
            chat.setGroupChat(i % 2 == 0);
            chat.setCreatedAt(LocalDateTime.now());
            chat.setPinned(i % 2 == 0);
            chatRepository.save(chat);
        }
    }

    public void populateMessages() {
        for (int i = 1; i <= 10; i++) {
            Message msg = messageFactory.createMessage(i % 2 == 0? "Text" : "Image");
            msg.setChatId(i % 2 == 0? UUID.fromString("77777777-7777-7777-7777-777777777777") : UUID.fromString("88888888-8888-8888-8888-888888888888"));
            msg.setSenderId(i % 2 == 0? UUID.fromString("11111111-1111-1111-1111-111111111111") : UUID.fromString("22222222-2222-2222-2222-222222222222"));
            msg.setTimestamp(LocalDateTime.now());
            msg.setStatus(i % 2 == 0? MessageStatus.DELIVERED : MessageStatus.SENT);
            if(msg instanceof TextMessage textMessage)
                textMessage.setTextContent("Hi! how are you?");
            else if(msg instanceof ImageMessage imageMessage){
                imageMessage.setCaption("GUC Logo");
                imageMessage.setImageUrl("data:image/png;base64,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");
            }
        messageRepository.save(msg);
        }
    }


    @Transactional
    public void createDummyData() {

        UUID user1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID user2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID user3 = UUID.fromString("33333333-3333-3333-3333-333333333333");

        Message text1 = new TextMessage(UUID.fromString("77777777-7777-7777-7777-777777777777"), user1, LocalDateTime.now().minusHours(3), MessageStatus.DELIVERED, "Hey, how are you?");
        Message text2 = new TextMessage(UUID.fromString("88888888-8888-8888-8888-888888888888"), user1, LocalDateTime.now().minusHours(3), MessageStatus.DELIVERED, "Hey, how are you?");
        Message text3 = new TextMessage(UUID.fromString("99999999-9999-9999-9999-999999999999"), user1, LocalDateTime.now().minusHours(3), MessageStatus.DELIVERED, "Hey, how are you?");

        Message image1 = new ImageMessage(UUID.fromString("77777777-7777-7777-7777-777777777777"), user2, LocalDateTime.now().minusHours(2), MessageStatus.SENT, "GUC Logo", "GUC Logo");
        Message image2 = new ImageMessage(UUID.fromString("88888888-8888-8888-8888-888888888888"), user2, LocalDateTime.now().minusHours(2), MessageStatus.SENT, "GUC Logo", "GUC Logo");
        Message image3 = new ImageMessage(UUID.fromString("99999999-9999-9999-9999-999999999999"), user2, LocalDateTime.now().minusHours(2), MessageStatus.SENT, "GUC Logo", "GUC Logo");

        Message video1 = new VideoMessage(UUID.fromString("77777777-7777-7777-7777-777777777777"), user3, LocalDateTime.now().minusDays(1), MessageStatus.SENT, "GUC Logo", 3, (long) 2.5);
        Message video2 = new VideoMessage(UUID.fromString("88888888-8888-8888-8888-888888888888"), user3, LocalDateTime.now().minusDays(1), MessageStatus.SENT, "GUC Logo", 3, (long) 2.5);
        Message video3 = new VideoMessage(UUID.fromString("99999999-9999-9999-9999-999999999999"), user3, LocalDateTime.now().minusDays(1), MessageStatus.SENT, "GUC Logo", 3, (long) 2.5);

        Chat chat1 = new Chat(
                UUID.fromString("77777777-7777-7777-7777-777777777777"),
                "chat1",
                Arrays.asList(user1, user2),
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now(),
                Arrays.asList(text1, image1, video1),
                true,
                false);
        chatRepository.save(chat1);

        Chat chat2 = new Chat(
                UUID.fromString("88888888-8888-8888-8888-888888888888"),
                "chat2",
                Arrays.asList(user1, user2, user3),
                LocalDateTime.now().minusDays(4),
                LocalDateTime.now().minusMinutes(2),
                Arrays.asList(text2, image2, video2),
                false,
                true);
        chatRepository.save(chat2);

        Chat chat3 = new Chat(
                UUID.fromString("99999999-9999-9999-9999-999999999999"),
                "chat3",
                Arrays.asList(user2, user3),
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusMinutes(10),
                Arrays.asList(text3, image3, video3),
                false,
                false);
        chatRepository.save(chat3);
    }
}

