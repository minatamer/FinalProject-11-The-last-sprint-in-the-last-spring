package com.example.SearchApp.service;

import com.example.SearchApp.Clients.UserClient;
import com.example.SearchApp.Clients.WallClient;
import com.example.SearchApp.Strategy.SearchStrategy;
import com.example.SearchApp.model.PostDTO;
import com.example.SearchApp.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {

     UserClient userClient;
     WallClient wallClient;
     Map<String, SearchStrategy<?>> strategyMap;


    @Autowired
    public SearchService(UserClient userClient, WallClient wallClient, Map<String, SearchStrategy<?>> strategyMap) {
        this.userClient = userClient;
        this.wallClient = wallClient;
        this.strategyMap = strategyMap;

    }


    //-----------------------------------------------Posts-----------------------------------------------------//
    public List<PostDTO> searchPosts(String searchQuery)
    {

        List<PostDTO> posts = wallClient.getAllPosts();
        @SuppressWarnings("unchecked")
        SearchStrategy<PostDTO> postStrategy = (SearchStrategy<PostDTO>) strategyMap.get("postSearchStrategy");
        return postStrategy.search(posts,searchQuery);
    }



    //-----------------------------------------------Users-----------------------------------------------------//

    public List<UserDTO> searchUsers(String searchQuery)
    {
        List<UserDTO> users = userClient.getUsers("");
        @SuppressWarnings("unchecked")
        SearchStrategy<UserDTO> userStrategy = (SearchStrategy<UserDTO>) strategyMap.get("userSearchStrategy");
        return userStrategy.search(users,searchQuery);
    }





}
