package com.example.UserApp.repository;

import com.example.UserApp.model.BlockedUser;
import com.example.UserApp.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, UUID> {
    List<BlockedUser> findByUser(User user);
    Optional<BlockedUser> findByUserAndBlockedUser(User user, User blockedUser);
    void deleteByUserAndBlockedUser(User user, User blockedUser);
}
