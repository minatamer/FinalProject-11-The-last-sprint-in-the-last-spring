package com.example.WallApp.service;

import com.example.WallApp.Clients.UserClient;
import com.example.WallApp.model.Notification;
import com.example.WallApp.model.Observer;
import com.example.WallApp.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService implements Observer {

    @Autowired
    private UserClient userClient;

    @Autowired
    private NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotifications(UUID userId) {
        return notificationRepository.findByReceiverId(userId);
    }

    @Override
    public void update(UUID receiverId, String message) {
        Notification notification = new Notification(receiverId, message);
        notificationRepository.save(notification);
    }
}
