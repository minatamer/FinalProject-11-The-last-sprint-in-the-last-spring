package com.example.UserApp.service;


import com.example.UserApp.model.User;
import com.example.UserApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @Transactional
    public void createDummyUsers() {
        User salah = new User(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "mohamedsalah",
                "salah@gmail.com",
                "password1",
                "0123456789",
                33,
                "Male",
                null,
                false,
                null
        );
        userRepository.save(salah);

        User messi = new User(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "leomessi",
                "messi@gmail.com",
                "password2",
                "0123456789",
                37,
                "Male",
                null,
                false,
                null
        );
        userRepository.save(messi);

        User amr = new User(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "amrdiab",
                "amr@gmail.com",
                "password3",
                "0123456789",
                60,
                "Male",
                null,
                true,
                null
        );
        userRepository.save(amr);
    }

}