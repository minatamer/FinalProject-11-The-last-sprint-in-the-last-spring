package com.example.WallApp.service;

import com.example.WallApp.dto.PostRequest;
import com.example.WallApp.model.Post;
import com.example.WallApp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post addPost(PostRequest postRequest) {
        if ((postRequest.getTextContent() == null || postRequest.getTextContent().isBlank()) &&
                (postRequest.getImageUrl() == null || postRequest.getImageUrl().isBlank())) {
            throw new IllegalArgumentException("Post must contain either text or an image.");
        }

        // Correct way to call the builder with Lombok
        Post post = Post.builder()
                .userId(postRequest.getUserId())   // Corrected field name
                .textContent(postRequest.getTextContent())  // Corrected field name
                .imageUrl(postRequest.getImageUrl())   // Corrected field name
                .build();

        return postRepository.save(post);
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

    public Optional<Post> likePost(Post post, String userId) {
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
//    public Optional<Post> sharePost(Post post, String userId) {
//        if (!post.getSharedBy().contains(userId)) {
//            post.getSharedBy().add(userId);
//            return Optional.of(postRepository.save(post));
//        }
//        return Optional.of(post);
//    }

    //  ME7TAGEEN KAMAN NE3MEL FUNCTION FEL SERVICE ESMAHA ADD FRIEND

}

