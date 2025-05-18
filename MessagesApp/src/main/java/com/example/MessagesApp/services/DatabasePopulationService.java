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
                imageMessage.setImageUrl("GUC_Logo.png");
            }
            System.out.println("Message" + msg.getId() + " is to be injected!");
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
        Message text3 = new TextMessage(UUID.fromString("99999999-9999-9999-9999-999999999999"), user2, LocalDateTime.now().minusHours(3), MessageStatus.DELIVERED, "Hey, how are you?");

        Message image1 = new ImageMessage(UUID.fromString("77777777-7777-7777-7777-777777777777"), user2, LocalDateTime.now().minusHours(2), MessageStatus.SENT, "GUC Logo", "GUC Logo");
        Message image2 = new ImageMessage(UUID.fromString("88888888-8888-8888-8888-888888888888"), user2, LocalDateTime.now().minusHours(2), MessageStatus.SENT, "GUC Logo", "GUC Logo");
        Message image3 = new ImageMessage(UUID.fromString("99999999-9999-9999-9999-999999999999"), user2, LocalDateTime.now().minusHours(2), MessageStatus.SENT, "GUC Logo", "GUC Logo");

        Message video1 = new VideoMessage(UUID.fromString("77777777-7777-7777-7777-777777777777"), user1, LocalDateTime.now().minusDays(1), MessageStatus.SENT, "GUC Logo", 3, (long) 2.5);
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

