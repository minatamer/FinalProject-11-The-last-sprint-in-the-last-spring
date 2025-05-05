package com.example.UserApp.service;

import com.example.UserApp.model.User;
import com.example.UserApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

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

    public void populateRandomUsers() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@gmail.com");
            user.setPassword("password" + i);
            user.setPhoneNumber("0100000000" + i);
            user.setAge(20 + i);
            user.setGender(i % 2 == 0 ? "Male" : "Female");
            userRepository.save(user);
        }
    }

    public String updateUser(String username, String email, int age, String phoneNumber, String gender, UUID userId) {
        try {
            userRepository.updateUser(username, email, age, phoneNumber, gender, userId);
            return "User updated";
        } catch (Exception e) {
            return "User not found";
        }
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteUser(userId);
    }
} 

