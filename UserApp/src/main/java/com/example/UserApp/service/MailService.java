package com.example.UserApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendOtpEmail(String to, String otp) {
        String subject = "Your One-Time Password (OTP)";
        String body = "Dear User,\n\n" +
                "Your One-Time Password (OTP) is: " + otp + "\n\n" +
                "Please use this OTP to complete your authentication process.\n\n" +
                "This OTP is valid for a limited time only.\n\n" +
                "Best regards,\nYour Support Team";

        sendEmail(to, subject, body);  // Call the general sendEmail method to send OTP
    }
}
