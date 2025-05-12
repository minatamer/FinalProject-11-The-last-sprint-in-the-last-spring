package com.example.WallApp.model;

import java.util.UUID;
import com.example.WallApp.model.Observer;
import com.example.WallApp.model.Subject;



public class PostObserver implements Observer {
    private String textContent;
    private String imageUrl;
    private Subject postData;

    public PostObserver(Subject postData) {
        this.postData = postData;
        postData.registerObserver(this);

    }


    @Override
    public void update(String textContent, String imageUrl) {
        this.textContent = textContent;
        this.imageUrl = imageUrl;
        System.out.println( " notified. Current post content: " + textContent +" with imageUrl: " + imageUrl);
    }
}

