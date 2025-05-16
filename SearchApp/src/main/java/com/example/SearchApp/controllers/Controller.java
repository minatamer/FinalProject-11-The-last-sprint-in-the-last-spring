package com.example.SearchApp.controllers;

import com.example.SearchApp.model.PostDTO;
import com.example.SearchApp.model.UserDTO;
import com.example.SearchApp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class Controller
{
    SearchService searchService;

    @Autowired
    public Controller(SearchService searchService) {
        this.searchService = searchService;
    }

    //-----------------------------------------------Posts-----------------------------------------------------//
    @PostMapping("/post/search")
    public List<PostDTO> searchPosts(@RequestParam String searchQuery)
    {
        return searchService.searchPosts(searchQuery);
    }


    //-----------------------------------------------Users-----------------------------------------------------//

    @PostMapping("/user/search")
    public List<UserDTO> searchUsers(@RequestParam String searchQuery)
    {

        return searchService.searchUsers(searchQuery);
    }



}
