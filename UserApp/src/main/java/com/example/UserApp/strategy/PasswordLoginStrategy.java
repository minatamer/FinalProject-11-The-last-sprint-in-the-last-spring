package com.example.UserApp.strategy;

import com.example.UserApp.model.User;
import com.example.UserApp.repository.UserRepository;
import com.example.UserApp.security.TokenManager;
import com.example.UserApp.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

public class PasswordLoginStrategy implements LoginStrategy {

    private final UserRepository userRepository;

    private MailService mailService;

    public PasswordLoginStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // Generates a 6-digit number
        return String.valueOf(otp);
    }

    @Override
    public String login(String email, String password) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            if (user.get().isTwoFactorEnabled()) {
                // Generate and send OTP if 2FA is enabled
                String otp = generateRandomOtp();
                user.get().setOtp(otp);
                User userS = user.get();
                userRepository.save(userS); // Save OTP in the user record
                // Send OTP via email (Assuming you have a method to send the OTP)
                mailService.sendOtpEmail(user.get().getEmail(), otp);
                return "OTP sent to email. Please verify your OTP.";
            } else {
                // If 2FA is not enabled, generate and return a token
                String token = UUID.randomUUID().toString();
                user.get().setToken(token);
                User userS = user.get();
                userRepository.save(userS);
                TokenManager.getInstance().addToken(token);
                return token;
            }
//            String token = UUID.randomUUID().toString();
//            user.get().setToken(token);
//            userRepository.save(user.get());
//
//            TokenManager.getInstance().addToken(token);
//            return token;
        }
        return null;
    }
}
