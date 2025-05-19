package com.example.SearchApp.repositories;

import com.example.SearchApp.model.Search;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SearchRepository extends MongoRepository<Search, String> {
    // You can add custom query methods here if needed
    List<Search> findByUserId(UUID userId);
}