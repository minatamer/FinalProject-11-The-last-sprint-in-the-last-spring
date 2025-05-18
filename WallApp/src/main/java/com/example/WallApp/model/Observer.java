package com.example.WallApp.model;

import java.util.UUID;

public interface Observer {
    void update(UUID receiverId, String message);
}


