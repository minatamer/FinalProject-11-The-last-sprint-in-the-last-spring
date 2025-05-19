package com.example.UserApp.service;

import com.example.UserApp.model.BlockedUser;
import com.example.UserApp.model.Friend;
import com.example.UserApp.model.User;
import com.example.UserApp.repository.BlockedUserRepository;
import com.example.UserApp.repository.FriendRepository;
import com.example.UserApp.repository.UserRepository;
import com.example.UserApp.security.TokenManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private BlockedUserRepository blockedUserRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    private MailService mailService;
    @CachePut(value = "user_cache", key = "#result.id")
    public User saveUser(User user) {
        user.setId(UUID.randomUUID());
        return userRepository.save(user);
    }

    @Cacheable(value = "user_cache", key = "#id")
    public User findUserById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return user.get();
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }


    public boolean checkUserExistence(UUID userId){
        return userRepository.existsById(userId);
    }

    public void populateRandomUsers() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@gmail.com");
            user.setPassword("password" + i);
            user.setPhoneNumber("0100000000" + i);
            user.setAge(20 + i);
            user.setGender(i % 2 == 0 ? "Male" : "Female");
            user.setTwoFactorEnabled(false);
            userRepository.save(user);
        }
    }
    @CachePut(value = "user_cache", key = "#userId")
    public String updateUser(String username, String password, String email, int age, String phoneNumber, String gender, UUID userId, boolean isTwoFactorEnabled) {
        try {
            // Fetch the current user from the database
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Only update fields that are provided (non-null)
            if (username != null) {
                user.setUsername(username);
            }
            if (email != null) {
                user.setEmail(email);
            }
            if (age != 0) {
                user.setAge(age);
            }
            if (phoneNumber != null) {
                user.setPhoneNumber(phoneNumber);
            }
            if (gender != null) {
                user.setGender(gender);
            }
            if (password != null) {
                user.setPassword(password);
            }
            if (isTwoFactorEnabled != false) {
                user.setTwoFactorEnabled(isTwoFactorEnabled);
            }
            // Save the updated user back to the database
            userRepository.save(user);

            // Send an update email notification
            mailService.sendEmail(
                    user.getEmail(),
                    "Account Update Notification",
                    "Hello " + user.getUsername() + ",\n\nYour account details were successfully updated on " + LocalDateTime.now() + ".\n\nIf this wasn't you, please contact support."
            );

            return "User updated successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @CacheEvict(value = "user_cache", key = "#userId")
    public void deleteUser(UUID userId) {
        userRepository.deleteUser(userId);
    }

    public String authenticate(String email, String password) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if the password is correct
            if (user.getPassword().equals(password)) {
                // Check if 2FA is enabled
                if (user.isTwoFactorEnabled()) {
                    // Generate and send OTP if 2FA is enabled
                    String otp = generateRandomOtp();
                    user.setOtp(otp);
                    userRepository.save(user); // Save OTP in the user record
                    // Send OTP via email (Assuming you have a method to send the OTP)
                    mailService.sendOtpEmail(user.getEmail(), otp);
                    return "OTP sent to email. Please verify your OTP.";
                } else {
                    // If 2FA is not enabled, generate and return a token
                    String token = UUID.randomUUID().toString();
                    user.setToken(token);
                    userRepository.save(user);
                    TokenManager.getInstance().addToken(token);
                    return token;
                }
            }
        }
        return null;
    }

    public String authenticateWithOtp(String email, String otp) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if 2FA is enabled and if the OTP matches
            if (user.isTwoFactorEnabled() && user.getOtp().equals(otp)) {
                // OTP is correct, generate a token
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                userRepository.save(user);
                TokenManager.getInstance().addToken(token);
                return token;
            }
        }
        return null;
    }


    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // Generates a 6-digit number
        return String.valueOf(otp);
    }
    public String getToken(UUID userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return userOpt.get().getToken();
        }
        return null;
    }

    public boolean logout(String token) {
        Optional<User> userOpt = userRepository.findByToken(token);
        userOpt.ifPresent(user -> {
            user.setToken(null);
            userRepository.save(user);
        });
        if (TokenManager.getInstance().isValidToken(token)) {
            TokenManager.getInstance().removeToken(token);
        }
        return true;
    }
    //    public boolean isValidToken(String token) {
//        if (token == null || token.isEmpty()) return false;
//        return userRepository.findByToken(token).isPresent();
//    }
    public boolean isValidToken(String token) {
        return TokenManager.getInstance().isValidToken(token);
    }

    //Blocking User functions
    @Transactional
    public void blockUser(UUID userId, UUID targetId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User target = userRepository.findById(targetId).orElseThrow(() -> new RuntimeException("Target user not found"));

        if (blockedUserRepository.findByUserAndBlockedUser(user, target).isEmpty()) {
            blockedUserRepository.save(new BlockedUser(user, target));
        }
    }

    @Transactional
    public void unblockUser(UUID userId, UUID targetId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User target = userRepository.findById(targetId).orElseThrow(() -> new RuntimeException("Target user not found"));

        blockedUserRepository.deleteByUserAndBlockedUser(user, target);
    }

    public List<UUID> getBlockedUserIds(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return blockedUserRepository.findByUser(user)
                .stream()
                .map(blocked -> blocked.getBlockedUser().getId())
                .toList();
    }

    //Friend functions

    @Transactional
    public ResponseEntity<?> addFriend(UUID userId, UUID friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found: " + friendId));

        if (!friendRepository.existsByUserAndFriend(user, friend)) {
            // Bidirectional friendship
            Friend newFriend = new Friend(null, user, friend);
            Friend newFriend2 = new Friend(null, friend, user);
            friendRepository.save(newFriend);
            friendRepository.save(newFriend2);
        }
        Map<String,String> body = Map.of(
                "message",friendId+ " and " + userId + " are friends now."
        );
        return  ResponseEntity.ok().body(body);
    }

    @Transactional
    public ResponseEntity<?> removeFriend(UUID userId, UUID friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found: " + friendId));

        friendRepository.deleteByUserAndFriend(user, friend);
        friendRepository.deleteByUserAndFriend(friend, user);
        Map<String,String> body = Map.of(
                "message",friendId+" has been removed from the followed users of "+ userId
        );
        return  ResponseEntity.ok().body(body);
    }

    public List<UUID> getFriends(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        return friendRepository.findAllByUser(user)
                .stream()
                .map(f -> f.getFriend().getId())
                .toList();
}

    public UUID getUserIdByToken(String token) {
        return userRepository.findByToken(token)
                .map(User::getId)
                .orElse(null);
    }


}