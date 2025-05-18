package com.example.WallApp.repository;

import com.example.WallApp.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface  PostRepository extends MongoRepository<Post, UUID> {
    List<Post> findByUserIdInOrSharedByIn(List<UUID> userIds, List<UUID> sharedByIds);

}
