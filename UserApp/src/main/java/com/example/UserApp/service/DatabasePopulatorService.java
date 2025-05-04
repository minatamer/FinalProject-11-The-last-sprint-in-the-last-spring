package com.example.UserApp.service;


import com.example.UserApp.model.User;
import com.example.UserApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabasePopulatorService {

    private final UserRepository userRepository;

    @Autowired
    public DatabasePopulatorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void populateUsers() {
        for (int i = 1; i <= 10; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@gmail.com");
            user.setPassword("password" + i);
            user.setPhoneNumber("010000000" + i);
            user.setAge(20 + i);
            user.setGender(i % 2 == 0 ? "Male" : "Female");
            userRepository.save(user);
        }
    }
}