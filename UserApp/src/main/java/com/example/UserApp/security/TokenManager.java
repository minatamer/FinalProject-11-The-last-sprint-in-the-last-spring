package com.example.UserApp.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TokenManager {

    private static TokenManager instance;
    private final Set<String> tokens;

    private TokenManager() {
        tokens = Collections.synchronizedSet(new HashSet<>());
    }

    public static synchronized TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    public void addToken(String token) {
        tokens.add(token);
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }

    public boolean isValidToken(String token) {
        return tokens.contains(token);
    }
}
