package com.example.SearchApp.service;

import com.example.SearchApp.Clients.UserClient;
import com.example.SearchApp.Clients.WallClient;
import com.example.SearchApp.Command.*;
import com.example.SearchApp.Strategy.SearchStrategy;
import com.example.SearchApp.model.PostDTO;
import com.example.SearchApp.model.Search;
import com.example.SearchApp.model.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.SearchApp.repositories.SearchRepository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    UserClient userClient;
    WallClient wallClient;
    Map<String, SearchStrategy<?>> strategyMap;
    FilterInvoker<PostDTO> postsFilterInvoker;
    FilterInvoker<UserDTO> userFilterInvoker;

    @Autowired
    SearchRepository searchRepository;

    @Autowired
    public SearchService(UserClient userClient, WallClient wallClient, Map<String, SearchStrategy<?>> strategyMap, FilterInvoker<PostDTO> postsFilterInvoker, FilterInvoker<UserDTO> userFilterInvoker) {
        this.userClient = userClient;
        this.wallClient = wallClient;
        this.strategyMap = strategyMap;
        this.postsFilterInvoker = postsFilterInvoker;
        this.userFilterInvoker = userFilterInvoker;
    }

    public List<Search> getAllSearches() {
        return searchRepository.findAll();
    }

    public List<Search> getSearchesByUserId(UUID userId) {
        return searchRepository.findByUserId(userId);
    }

    //-----------------------------------------------Posts-----------------------------------------------------//
    public List<PostDTO> searchPosts(String searchQuery, String token)
    {
        UUID userId = userClient.getUserIdByToken(token)
                .get("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found for the specified token");
        }
        searchRepository.save(new Search(searchQuery, userId));
        // --- 2. Persist search --------------------------------------------------

        postsFilterInvoker.setCommandHistory( new ArrayList<>());
        List<PostDTO> posts = wallClient.getAllPosts();
        @SuppressWarnings("unchecked")
        SearchStrategy<PostDTO> postStrategy = (SearchStrategy<PostDTO>) strategyMap.get("postSearchStrategy");
        return postStrategy.search(posts,searchQuery);
    }

    public List<PostDTO> filterPosts(List<PostDTO> posts,
                                     boolean likes, int minLikes,   int maxLikes,
                                     boolean shares, int minShares,  int maxShares,
                                     boolean date, LocalDate startDate ,  LocalDate endDate)
    {
        List<PostDTO> filteredPosts = new ArrayList<>(posts);

        if(likes)
        {
            postsFilterInvoker.setCommand(new FilterPostByLikesCommand(minLikes, maxLikes));
            filteredPosts = postsFilterInvoker.filter(filteredPosts);
        }
        if (shares)
        {
            postsFilterInvoker.setCommand(new FilterPostBySharesCommand(minShares, maxShares));
            filteredPosts = postsFilterInvoker.filter(filteredPosts);
        }
        if(date)
        {
            postsFilterInvoker.setCommand(new FilterPostByDateCommand(startDate, endDate));
            filteredPosts = postsFilterInvoker.filter(filteredPosts);
        }
        return filteredPosts.stream().distinct().collect(Collectors.toList());
    }


    public List<PostDTO> undoFilterPosts(boolean likes, boolean shares, boolean date) {
        List<FilterCommand<PostDTO>> commandHistory = postsFilterInvoker.getCommandHistory();
        List<PostDTO> undonePosts = new ArrayList<>();

        // Step 1: Identify the command to undo (first matching one)
        FilterCommand<PostDTO> toUndo = null;
        for (FilterCommand<PostDTO> command : commandHistory) {
            String className = command.getClass().getSimpleName();
            if ((className.equals("FilterPostByLikesCommand") && likes) ||
                    (className.equals("FilterPostBySharesCommand") && shares) ||
                    (className.equals("FilterPostByDateCommand") && date)) {
                toUndo = command;
                break;
            }
        }

        if (toUndo != null) {
            // Undo that filter
            undonePosts = toUndo.undo();
            commandHistory.remove(toUndo);
        }

        // Step 2: Remove other filters that are also requested to be undone
        Iterator<FilterCommand<PostDTO>> iterator = commandHistory.iterator();
        while (iterator.hasNext()) {
            FilterCommand<PostDTO> command = iterator.next();
            String className = command.getClass().getSimpleName();

            if ((className.equals("FilterPostByLikesCommand") && likes) ||
                    (className.equals("FilterPostBySharesCommand") && shares) ||
                    (className.equals("FilterPostByDateCommand") && date)) {
                iterator.remove(); // ✅ safe removal
            }
        }

        // Step 3: Re-apply remaining filters (if any)
        for (FilterCommand<PostDTO> command : commandHistory) {
            undonePosts = command.execute(undonePosts);
        }

        return undonePosts;
    }

    public List<PostDTO> sortPosts(List<PostDTO> posts, String sortCriteria, String sortOrder) {
        Comparator<PostDTO> comparator;
        sortCriteria = sortCriteria.toLowerCase().trim();
        sortOrder = sortOrder.toLowerCase().trim();

        switch (sortCriteria.toLowerCase().trim()) {
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

    public List<UserDTO> searchUsers(String searchQuery , String token)
    {
        UUID userId = userClient.getUserIdByToken(token)
                .get("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found for the specified token");
        }
        searchRepository.save(new Search(searchQuery, userId));
        userFilterInvoker.setCommandHistory( new ArrayList<>());
        List<UserDTO> users = userClient.getUsers(token);
        @SuppressWarnings("unchecked")
        SearchStrategy<UserDTO> userStrategy = (SearchStrategy<UserDTO>) strategyMap.get("userSearchStrategy");
        return userStrategy.search(users,searchQuery);
    }

    public List<UserDTO> filterUsers(List<UserDTO> users,
                                     boolean age, int minAge, int maxAge,
                                     boolean gender, String genderToMatch) {
        List<UserDTO> filteredUsers = new ArrayList<>(users);

        if (age) {
            userFilterInvoker.setCommand(new FilterUserByAgeCommand(minAge, maxAge));
            filteredUsers = userFilterInvoker.filter(filteredUsers);
        }


        if (gender) {
            userFilterInvoker.setCommand(new FilterUserByGenderCommand(genderToMatch));
            filteredUsers = userFilterInvoker.filter(filteredUsers);
        }

        return filteredUsers.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<UserDTO> undoFilterUsers(boolean age, boolean gender) {
        List<FilterCommand<UserDTO>> commandHistory = userFilterInvoker.getCommandHistory();

        List<String> filtersToUndo = new ArrayList<>();
        if (age) filtersToUndo.add("FilterUserByAgeCommand");
        if (gender) filtersToUndo.add("FilterUserByGenderCommand");

        List<FilterCommand<UserDTO>> originalHistory = new ArrayList<>(commandHistory);

        List<UserDTO> baseUsers = new ArrayList<>();
        for (FilterCommand<UserDTO> command : originalHistory) {
            String className = command.getClass().getSimpleName();
            if (filtersToUndo.contains(className)) {
                baseUsers = command.undo();
                break;
            }
        }

        if (baseUsers.isEmpty()) return new ArrayList<>();

        commandHistory.removeIf(cmd -> filtersToUndo.contains(cmd.getClass().getSimpleName()));

        List<UserDTO> result = baseUsers;
        for (FilterCommand<UserDTO> command : commandHistory) {
            result = command.execute(result);
        }

        return result;
    }

    public List<UserDTO> sortUsers(List<UserDTO> users, String sortCriteria, String sortOrder) {
        Comparator<UserDTO> comparator;
        sortCriteria = sortCriteria.toLowerCase().trim();
        sortOrder = sortOrder.toLowerCase().trim();

        switch (sortCriteria.toLowerCase()) {
            case "age":
                comparator = Comparator.comparingInt(UserDTO::getAge);
                break;
            case "gender":
                comparator = Comparator.comparing(UserDTO::getGender);
                break;
            default:
                throw new IllegalArgumentException("Unknown sort criteria: " + sortCriteria);
        }

        if ("desc".equals(sortOrder)) {
            comparator = comparator.reversed();
        }

        return users.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}

