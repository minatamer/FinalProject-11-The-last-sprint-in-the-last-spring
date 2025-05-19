package com.example.UserApp.controller;

import com.example.UserApp.model.User;
import com.example.UserApp.repository.UserRepository;
import com.example.UserApp.service.UserService;
import com.example.UserApp.service.DatabasePopulatorService;
import com.example.UserApp.strategy.LoginContext;
import com.example.UserApp.strategy.OtpLoginStrategy;
import com.example.UserApp.strategy.PasswordLoginStrategy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserRepository userRepository;
    private final DatabasePopulatorService databasePopulatorService;

    // Toggle this to enable/disable authentication globally
    private final boolean authenticationEnabled = true;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, DatabasePopulatorService databasePopulatorService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.databasePopulatorService = databasePopulatorService;
    }

    private boolean isAuthenticated(String token) {
        return !authenticationEnabled || (token != null && userService.isValidToken(token));
    }

    @PostMapping("/populateRandom")
    public void populateRandom() {
        userService.populateRandomUsers();
        databasePopulatorService.populateUsers();
    }

    @PostMapping("/dummyData")
    public void createDummyData() {
        databasePopulatorService.createDummyUsers();
    }

    @DeleteMapping("/wipe")
    public ResponseEntity<String> wipeAllUsers() {
        userRepository.deleteAll();
        return ResponseEntity.ok("All users have been deleted successfully.");
    }


    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody User user,
                                        @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        return ResponseEntity.ok(userService.saveUser(user));
    }

    @GetMapping("/")
    public ResponseEntity<?> getUsers(@RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId,
                                         @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId,
                                        @RequestBody User user,
                                        @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        String result = userService.updateUser(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getAge(),
                user.getPhoneNumber(),
                user.getGender(),
                userId,
                user.isTwoFactorEnabled()
        );
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId,
                                        @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted.");
    }


//   START OF METHODS NEEDED IN WALL APP

    @GetMapping("check/{userId}")
    public boolean checkUser(@PathVariable UUID userId){
        return userService.checkUserExistence(userId);
    }

//    END OF METHODS NEEDED FOR WALLAPP


//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
//        String email = credentials.get("email");
//        String password = credentials.get("password");
//
//        String token = userService.authenticate(email, password);
//        if (token != null) {
//            return ResponseEntity.ok(Collections.singletonMap("token", token));
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
//        }
//    }
//
//    @PostMapping("/otpLogin")
//    public ResponseEntity<?> authenticateWithOtp(@RequestBody Map<String, String> credentials) {
//        String email = credentials.get("email");
//        String otp = credentials.get("otp");
//
//        String token = userService.authenticateWithOtp(email, otp);
//        if (token != null) {
//            return ResponseEntity.ok(Collections.singletonMap("token", token));
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP.");
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        LoginContext context = new LoginContext();
        context.setStrategy(new PasswordLoginStrategy(userRepository));
        String token = context.executeStrategy(email, password);

        if (token != null) {
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/otpLogin")
    public ResponseEntity<?> otpLogin(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String otp = credentials.get("otp");

        LoginContext context = new LoginContext();
        context.setStrategy(new OtpLoginStrategy(userRepository));
        String token = context.executeStrategy(email, otp);

        if (token != null) {
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        boolean success = userService.logout(token);
        if (success) {
            return ResponseEntity.ok("User logged out successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }

    //Blocking User APIs
    @PostMapping("/{userId}/block/{targetId}")
    public ResponseEntity<String> blockUser(@PathVariable UUID userId,
                                            @PathVariable UUID targetId,
                                            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        userService.blockUser(userId, targetId);
        return ResponseEntity.ok("User blocked successfully.");
    }

    @DeleteMapping("/{userId}/unblock/{targetId}")
    public ResponseEntity<String> unblockUser(@PathVariable UUID userId,
                                              @PathVariable UUID targetId,
                                              @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        userService.unblockUser(userId, targetId);
        return ResponseEntity.ok("User unblocked successfully.");
    }

    @GetMapping("/{userId}/blocked-users")
    public ResponseEntity<?> getBlockedUserIds(@PathVariable UUID userId,
                                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        List<UUID> blockedUserIds = userService.getBlockedUserIds(userId);
        return ResponseEntity.ok(blockedUserIds);
    }

// Friend APIs

    @PostMapping("/{userId}/friend/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable UUID userId,
                                       @PathVariable UUID friendId,
                                       @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        return ResponseEntity.ok(userService.addFriend(userId, friendId));
    }

    @DeleteMapping("/{userId}/unfriend/{friendId}")
    public ResponseEntity<?> removeFriend(@PathVariable UUID userId,
                                          @PathVariable UUID friendId,
                                          @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        userService.removeFriend(userId, friendId);
        return ResponseEntity.ok("Friend removed.");
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<?> getFriends(@PathVariable UUID userId,
                                        @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        return ResponseEntity.ok(userService.getFriends(userId));
    }


    @GetMapping("/validate-token/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("valid", false, "message", "Missing token"));
        }

        boolean valid = userService.isValidToken(token);
        return ResponseEntity.ok(Map.of("valid", valid));
    }

    @GetMapping("/{userId}/token")
    public ResponseEntity<?> getUserToken(@PathVariable UUID userId) {

        String userToken = userService.getToken(userId);
        if (userToken != null) {
            return ResponseEntity.ok(Map.of("token", userToken));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found for the specified user.");
        }
    }
    
    
    




}