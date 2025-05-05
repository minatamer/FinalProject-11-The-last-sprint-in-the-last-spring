package com.example.WallApp.service;

import com.example.WallApp.dto.PostRequest;
import com.example.WallApp.model.Post;
import com.example.WallApp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Post post = Post.builder()
                .userId(UUID.fromString(postRequest.getUserId()))
                .textContent(postRequest.getTextContent())
                .imageUrl(postRequest.getImageUrl())
                .build();

        return postRepository.save(post);
    }

    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    public Optional<Post> likePost(Post post, String userId) {
        if (!post.getLikedBy().contains(userId)) {
            post.getLikedBy().add(userId);
            return Optional.of(postRepository.save(post));
        }
        return Optional.of(post);
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

