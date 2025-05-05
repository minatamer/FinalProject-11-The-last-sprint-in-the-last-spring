package com.example.UserApp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private int age;
    private String gender;
    private LocalDateTime createdAt = LocalDateTime.now();

    // 1. List of Friend IDs
    @ElementCollection
    private List<UUID> friendIds = new ArrayList<>();

    // 2. List of My Post IDs
    @ElementCollection
    private List<UUID> myPostIds = new ArrayList<>();

    // 3. List of Shared Post IDs
    @ElementCollection
    private List<UUID> sharedPostIds = new ArrayList<>();

    public User() {}

    public User(String username, String email, String password, String phoneNumber, int age, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.gender = gender;
    }

    // Getters and setters...

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<UUID> getFriendIds() { return friendIds; }
    public void setFriendIds(List<UUID> friendIds) { this.friendIds = friendIds; }

    public List<UUID> getMyPostIds() { return myPostIds; }
    public void setMyPostIds(List<UUID> myPostIds) { this.myPostIds = myPostIds; }

    public List<UUID> getSharedPostIds() { return sharedPostIds; }
    public void setSharedPostIds(List<UUID> sharedPostIds) { this.sharedPostIds = sharedPostIds; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", friends=" + friendIds +
                ", myPosts=" + myPostIds +
                ", sharedPosts=" + sharedPostIds +
                '}';
    }
}

