package com.example.WallApp.service;

import com.example.WallApp.Clients.UserClient;
import com.example.WallApp.dto.PostRequest;
import com.example.WallApp.model.Follows;
import com.example.WallApp.model.Post;
import com.example.WallApp.repository.FollowRepository;
import com.example.WallApp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    PostRepository postRepository;
    FollowRepository followRepository;

    @Autowired
    UserClient userClient;

    @Autowired
    public PostService(PostRepository postRepository, FollowRepository followRepository) {
        this.postRepository = postRepository;
        this.followRepository = followRepository;
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

        // Add post to the user's list of posts via UserClient
        userClient.addPost(postRequest.getUserId(), post.getId());
        postRepository.save(post);
        return post ;
    }

    public Optional<Post> getPostById(UUID id) {
        return postRepository.findById(id);
    }

    public Post updatePost(UUID id,PostRequest postreq) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post retrievedPost = optionalPost.get();

            if ((postreq.getTextContent() == null || postreq.getTextContent().isBlank()) &&
                    (postreq.getImageUrl() == null || postreq.getImageUrl().isBlank())
                    ||(postreq.getTextContent() == null && retrievedPost.getTextContent() != null && postreq.getImageUrl() == null && retrievedPost.getImageUrl() == null)
                    ||(postreq.getTextContent() == null && retrievedPost.getTextContent() == null && postreq.getImageUrl() == null && retrievedPost.getImageUrl() != null)) {
                throw new IllegalArgumentException("Post cannot be empty. Please provide either text content or an image URL.");
            }

            retrievedPost.setTextContent(postreq.getTextContent());
            retrievedPost.setImageUrl(postreq.getImageUrl());

            // Save and return the updated post
            return postRepository.save(retrievedPost);
        }
        return null;
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
            return Optional.of(postRepository.save(post));
        }
        return Optional.of(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }


    //ME7TAGEEN NE LINK BEL USER MESH EL POST


    public List<UUID> getUsersIFollow(UUID myUserId) {
        List<Follows> following = followRepository.findByFollowerId(myUserId);
        return following.stream()
                .map(Follows::getFollowedId)
                .collect(Collectors.toList());
    }
    public ResponseEntity addFriend(UUID userId, UUID followedId) {
        ResponseEntity<Boolean> responseFollower = userClient.checkUser(userId);
        if (responseFollower.getBody() == null || !responseFollower.getBody()) {
            throw new RuntimeException("User not found");
        }
        ResponseEntity<Boolean> responseFollowed = userClient.checkUser(userId);
        if (responseFollowed.getBody() == null || !responseFollowed.getBody()) {
            throw new RuntimeException("User not found");
        }

        if (userId.equals(followedId)) {
            throw new RuntimeException("A user cannot follow themselves");
        }

        Follows friendship = new Follows();
        friendship.setFollowerId(userId);
        friendship.setFollowedId(followedId);
        followRepository.save(friendship); // This will auto-insert with an auto-generated ID
        Map<String, String> body = Map.of(
                "message", "friend " + followedId + " has been added to the friends list of the user " + userId
        );
        return ResponseEntity.ok(body);
    }

    public ResponseEntity<?> sharePost(UUID userId, UUID postId) {
        return userClient.setSharedPost(userId, postId);
    }




}

