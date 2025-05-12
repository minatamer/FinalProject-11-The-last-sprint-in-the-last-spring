package com.example.WallApp.model;

import com.example.WallApp.model.Observer;

public interface Subject{
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}

