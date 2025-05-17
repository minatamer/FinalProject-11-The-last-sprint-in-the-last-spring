package com.example.SearchApp.controllers;

import com.example.SearchApp.model.PostDTO;
import com.example.SearchApp.model.UserDTO;
import com.example.SearchApp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @PostMapping("/post/filter")
    public List<PostDTO> filterPosts(@RequestParam List<PostDTO> posts,
                                     @RequestParam Boolean likes, @RequestParam Integer minLikes,  @RequestParam Integer maxLikes,
                                     @RequestParam Boolean shares, @RequestParam Integer minShares, @RequestParam Integer maxShares,
                                     @RequestParam Boolean date, @RequestParam LocalDate startDate , @RequestParam LocalDate endDate)
    {
        return searchService.filterPosts(posts, likes, minLikes, maxLikes, shares, minShares, maxShares, date, startDate, endDate);
    }

    @PostMapping("/post/undoFilters")
    public List<PostDTO> undoFilterPosts( @RequestParam Boolean likes, @RequestParam Boolean shares, @RequestParam Boolean date)
    {
        return searchService.undoFilterPosts(likes, shares, date);
    }

    @PostMapping("/post/sort")
    public List<PostDTO> sortPosts(@RequestParam List<PostDTO> posts, @RequestParam String sortCriteria , @RequestParam String sortOrder)
    {
        return searchService.sortPosts(posts, sortCriteria, sortOrder);
    }

    //-----------------------------------------------Users-----------------------------------------------------//

    @PostMapping("/user/search")
    public List<UserDTO> searchUsers(@RequestParam String searchQuery)
    {
        return searchService.searchUsers(searchQuery);
    }

    @PostMapping("/user/filter")
    public List<UserDTO> filterUsers(@RequestParam List<UserDTO> users,
                                     @RequestParam Boolean age, @RequestParam Integer minAge,  @RequestParam Integer maxAge,
                                     @RequestParam Boolean date, @RequestParam LocalDate startDate , @RequestParam LocalDate endDate)
    {
        return searchService.filterUsers(users,age,minAge,maxAge,date,startDate,endDate);
    }

    @PostMapping("/user/undoFilters")
    public List<UserDTO> undoFilterUsers( @RequestParam Boolean age, @RequestParam Boolean date)
    {
        return searchService.undoFilterUsers(age,date);
    }

    @PostMapping("/user/sort")
    public List<UserDTO> sortUsers(@RequestParam List<UserDTO> users,@RequestParam String sortCriteria , @RequestParam String sortOrder)
    {
        return searchService.sortUsers(users, sortCriteria, sortOrder);
    }

}
