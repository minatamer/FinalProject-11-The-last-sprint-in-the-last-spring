package com.example.UserApp.repository;

import com.example.UserApp.model.Friend;
import com.example.UserApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, UUID> {
    boolean existsByUserAndFriend(User user, User friend);
    void deleteByUserAndFriend(User user, User friend);
    List<Friend> findAllByUser(User user);
}
