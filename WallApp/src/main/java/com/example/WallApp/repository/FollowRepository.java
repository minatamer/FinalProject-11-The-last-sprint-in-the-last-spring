package com.example.WallApp.repository;

import com.example.WallApp.model.Follows;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FollowRepository extends MongoRepository<Follows, UUID> {

    List<Follows> findByFollowerId(UUID followerId);

    List<Follows> findByFollowedId(UUID followedId);

    List<Follows> findByFollowerIdAndFollowedId(UUID followerId, UUID followedId);
}
