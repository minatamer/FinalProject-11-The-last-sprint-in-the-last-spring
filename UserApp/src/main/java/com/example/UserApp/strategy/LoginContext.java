package com.example.UserApp.strategy;

public class LoginContext {
    private LoginStrategy strategy;

    public void setStrategy(LoginStrategy strategy) {
        this.strategy = strategy;
    }

    public String executeStrategy(String email, String credential) {
        return strategy.login(email, credential);
    }
}