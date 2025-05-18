package com.example.WallApp.model;

import java.util.UUID;

public interface Subject{
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(UUID receiverId, String message);
}

