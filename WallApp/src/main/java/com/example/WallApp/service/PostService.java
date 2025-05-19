package com.example.WallApp.service;

import com.example.WallApp.Clients.UserClient;
import com.example.WallApp.dto.PostRequest;
import com.example.WallApp.model.Observer;
import com.example.WallApp.model.Post;
import com.example.WallApp.model.Subject;
import com.example.WallApp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class PostService implements Subject {

    // List of observers (users who observe posts)
    private final List<Observer> observers = new ArrayList<>();


    @Autowired
    PostRepository postRepository;

    @Autowired
    UserClient userClient;

    @Autowired
    private NotificationService notificationService; // observer

    @Autowired
    public PostService(PostRepository postRepository, NotificationService notificationService, UserClient userClient) {
        this.notificationService = notificationService;
        this.userClient = userClient;
        this.postRepository = postRepository;
    }

    @Transactional
    public ResponseEntity<List<Post>> populateDummyPosts(){

        Post postSalah = new Post.PostBuilder()
                .Id(UUID.fromString("44444444-4444-4444-4444-444444444444"))
                .UserId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .TextContent("Post Salah text content")
                .ImageUrl("Post Salah image URL")
                .build();
        postRepository.save(postSalah);

        Post postMessi = new Post.PostBuilder()
                .Id(UUID.fromString("55555555-5555-5555-5555-555555555555"))
                .UserId( UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .TextContent("Post Messi text content")
                .ImageUrl("Post Messi image URL")
                .build();
        postRepository.save(postMessi);

        Post post3amoora = new Post.PostBuilder()
                .Id(UUID.fromString("66666666-6666-6666-6666-666666666666"))
                .UserId( UUID.fromString("33333333-3333-3333-3333-333333333333"))
                .TextContent("Post 3amoora text content")
                .ImageUrl("Post 3amoora image URL")
                .build();
        postRepository.save(post3amoora);

        return ResponseEntity.ok(postRepository.findAll());

    }

    public Post addPost(PostRequest postRequest) {
        // Validate the post content
        if ((postRequest.getTextContent() == null || postRequest.getTextContent().isBlank()) &&
                (postRequest.getImageUrl() == null || postRequest.getImageUrl().isBlank())) {
            throw new IllegalArgumentException("Post must contain either text or an image.");
        }

        // Create and save the post
        Post post = new Post.PostBuilder()
                .UserId(postRequest.getUserId())
                .TextContent(postRequest.getTextContent())
                .ImageUrl(postRequest.getImageUrl())
                .build();

        return postRepository.save(post);
    }

    public Optional<Post> getPostById(UUID id) {
        return postRepository.findById(id);
    }

    public List<Post> getMyPosts(UUID userId , String token) {
        // 1. Get the user's friends
        ResponseEntity<List<UUID>> friendsResponse = userClient.getFriends(userId , token);

        if (friendsResponse.getStatusCode().is2xxSuccessful() && friendsResponse.getBody() != null) {
            List<UUID> friends = friendsResponse.getBody();

            // 2. Query posts by friends (created or shared)
            return postRepository.findByUserIdInOrSharedByIn(friends, friends);
        } else {
            // If no friends found or error, return an empty list
            return new ArrayList<>();
        }
    }


    public Post updatePost(UUID id, PostRequest postReq) {
        // Retrieve the existing post
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            throw new IllegalArgumentException("Post with id " + id + " not found");
        }

        Post existingPost = optionalPost.get();

        // Merge the request data into the existing post
        if (postReq.getTextContent() != null) {
            existingPost.setTextContent(postReq.getTextContent());
        }
        if (postReq.getImageUrl() != null) {
            existingPost.setImageUrl(postReq.getImageUrl());
        }

        // Validation to ensure no field is null
        validatePost(existingPost);

        // Save the updated post
        return postRepository.save(existingPost);
    }

    private void validatePost(Post post) {
        if (post.getTextContent() == null && post.getImageUrl() == null) {
            throw new IllegalArgumentException("Post cannot have null fields");
        }
    }


    public void deletePostById(UUID id) {
        postRepository.deleteById(id);
    }
    public void deleteAllPosts() {
        postRepository.deleteAll();
    }

    public Optional<Post> likePost(Post post, UUID userId) {
        if (!post.getLikedBy().contains(userId)) {
            post.getLikedBy().add(userId);

            // Notify post owner of the like
            String message = "User with ID " + userId + " liked your post.";
            notifyObservers(post.getUserId(), message);


            return Optional.of(postRepository.save(post));
        }
        return Optional.of(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }


    //ME7TAGEEN NE LINK BEL USER MESH EL POST


    public ResponseEntity<?> addFriend(UUID userId, UUID friendId , String token) {
        // Register the friend as an observer
        registerObserver(notificationService);

        return userClient.addFriend(userId, friendId , token);
    }

    public ResponseEntity<?> removeFriend(UUID userId, UUID friendId , String token) {
        // Unregister the friend as an observer
        removeObserver(notificationService);

        return userClient.removeFriend(userId, friendId , token);
    }

    public ResponseEntity<?> sharePost(UUID userId, UUID postId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            List<UUID> shares = post.getSharedBy();

            if (!shares.contains(userId)) {
                shares.add(userId); // Add to shared list
                post.setSharedBy(shares);
                postRepository.save(post);

                // Notify post owner about the share
                String message = "User with ID " + userId + " shared your post.";
                notifyObservers(post.getUserId(), message);

                return ResponseEntity.ok("Post shared successfully.");
            } else {
                return ResponseEntity.badRequest().body("You already shared this post.");
            }
        } else {
            return ResponseEntity.badRequest().body("Post not found.");
        }
    }


    public ResponseEntity<List<UUID>> getFriends(@PathVariable UUID userId , String token){return userClient.getFriends(userId , token);}

    @Override
    public void registerObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o); // Add observer to the list
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o); // Remove observer from the list
    }


    @Override
    public void notifyObservers(UUID receiverId, String message) {
        // Notify all observers
        for (Observer observer : observers) {
            observer.update(receiverId, message);
        }

    }

}