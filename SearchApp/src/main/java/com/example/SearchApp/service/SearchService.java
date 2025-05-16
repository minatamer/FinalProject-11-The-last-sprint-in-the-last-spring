package com.example.SearchApp.service;

import com.example.SearchApp.Clients.UserClient;
import com.example.SearchApp.Clients.WallClient;
import com.example.SearchApp.Command.*;
import com.example.SearchApp.Strategy.SearchStrategy;
import com.example.SearchApp.model.PostDTO;
import com.example.SearchApp.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

     UserClient userClient;
     WallClient wallClient;
     Map<String, SearchStrategy<?>> strategyMap;
     FilterInvoker<PostDTO> postsFilterInvoker;


    @Autowired
    public SearchService(UserClient userClient, WallClient wallClient, Map<String, SearchStrategy<?>> strategyMap, FilterInvoker<PostDTO>postsFilterInvoker) {
        this.userClient = userClient;
        this.wallClient = wallClient;
        this.strategyMap = strategyMap;
        this.postsFilterInvoker = postsFilterInvoker;

    }


    //-----------------------------------------------Posts-----------------------------------------------------//
    public List<PostDTO> searchPosts(String searchQuery)
    {
        postsFilterInvoker.setCommandHistory( new ArrayList<>());
        List<PostDTO> posts = wallClient.getAllPosts();
        @SuppressWarnings("unchecked")
        SearchStrategy<PostDTO> postStrategy = (SearchStrategy<PostDTO>) strategyMap.get("postSearchStrategy");
        return postStrategy.search(posts,searchQuery);
    }

    public List<PostDTO> filterPosts(List<PostDTO> posts,
                                     boolean likes, int minLikes, int maxLikes,
                                     boolean shares, int minShares, int maxShares,
                                     boolean date, LocalDate startDate , LocalDate endDate)
    {
        List<PostDTO> filteredPosts = new ArrayList<>(posts);

        if(likes)
        {
            postsFilterInvoker.setCommand(new FilterPostByLikesCommand(minLikes, maxLikes));
            filteredPosts = postsFilterInvoker.filter(filteredPosts);
        }
        else if (shares)
        {
            postsFilterInvoker.setCommand(new FilterPostBySharesCommand(minShares, maxShares));
            filteredPosts = postsFilterInvoker.filter(filteredPosts);
        }
        else if(date)
        {
            postsFilterInvoker.setCommand(new FilterPostByDateCommand(startDate, endDate));
            filteredPosts = postsFilterInvoker.filter(filteredPosts);
        }
        return filteredPosts;
    }


    public List<PostDTO> undoFilterPosts(boolean likes, boolean shares, boolean date)
    {
        List<FilterCommand<PostDTO>> commandHistory = postsFilterInvoker.getCommandHistory();
        List<PostDTO> undonePosts = new ArrayList<>();

        // Get the earliest version of the posts using reflection
        for (FilterCommand<PostDTO> command : commandHistory)
        {
            String className = command.getClass().getSimpleName();

            if ( (className.equals("FilterPostByLikesCommand") && likes) || (className.equals("FilterPostBySharesCommand") && shares)
                    || (className.equals("FilterPostByDateCommand") && date) )
            {
                undonePosts = command.undo();
                commandHistory.remove(command);
                break;
            }

        }

        //Remove the filters that are supposed to be undone you got the earliest version so by that u can apply the filters
        //that aren't supposed to be undone safely
        for (FilterCommand<PostDTO> command : commandHistory)
        {
            String className = command.getClass().getSimpleName();

            if ( (className.equals("FilterPostByLikesCommand") && likes) || (className.equals("FilterPostBySharesCommand") && shares)
                    || (className.equals("FilterPostByDateCommand") && date) )
                commandHistory.remove(command);

        }

        //Apply remaining filters
        for (FilterCommand<PostDTO> command : commandHistory)
            undonePosts = command.execute(undonePosts);


        return undonePosts;
    }

    public List<PostDTO> sortPosts(List<PostDTO> posts, String sortCriteria, String sortOrder) {
        Comparator<PostDTO> comparator;
        sortCriteria = sortCriteria.toLowerCase().trim();
        sortOrder = sortOrder.toLowerCase().trim();

        switch (sortCriteria.toLowerCase()) {
            case "likes":
                comparator = Comparator.comparingInt(p -> p.getLikedBy().size());
                break;
            case "shares":
                comparator = Comparator.comparingInt(p -> p.getSharedBy().size());
                break;
            case "date":
                comparator = Comparator.comparing(PostDTO::getCreatedAt);
                break;
            default:
                throw new IllegalArgumentException("Unknown sort criteria: " + sortCriteria);
        }

        if ("desc".equals(sortOrder)) {
            comparator = comparator.reversed();
        }

        return posts.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
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
