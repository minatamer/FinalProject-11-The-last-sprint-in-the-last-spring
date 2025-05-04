package com.example.UserApp.controller;

import com.example.UserApp.model.User;
import com.example.UserApp.service.UserService;
import com.example.UserApp.service.DatabasePopulatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final DatabasePopulatorService databasePopulatorService;

    @Autowired
    public UserController(UserService userService, DatabasePopulatorService databasePopulatorService) {
        this.userService = userService;
        this.databasePopulatorService = databasePopulatorService;
    }

    @PostMapping("/populateRandom")
    public void populateRandom() {
        userService.populateRandomUsers();
        databasePopulatorService.populateUsers();
    }

    @PostMapping("/")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/")
    public List<User> getUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return userService.findUserById(userId);
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable UUID userId, @RequestBody User user) {
        return userService.updateUser(
                user.getUsername(),
                user.getEmail(),
                user.getAge(),
                user.getPhoneNumber(),
                user.getGender(),
                userId
        );
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }
}