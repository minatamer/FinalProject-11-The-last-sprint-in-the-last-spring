package com.example.UserApp.strategy;

import com.example.UserApp.model.User;
import com.example.UserApp.repository.UserRepository;
import com.example.UserApp.security.TokenManager;

import java.util.Optional;
import java.util.UUID;

public class OtpLoginStrategy implements LoginStrategy {

    private final UserRepository userRepository;

    public OtpLoginStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String login(String email, String otp) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent() && user.get().getOtp().equals(otp)) {
            String token = UUID.randomUUID().toString();
            user.get().setToken(token);
            userRepository.save(user.get());

            TokenManager.getInstance().addToken(token);
            return token;
        }
        return null;
    }
}
