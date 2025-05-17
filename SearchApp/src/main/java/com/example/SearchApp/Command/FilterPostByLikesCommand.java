package com.example.SearchApp.Command;

import com.example.SearchApp.model.PostDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterPostByLikesCommand implements FilterCommand<PostDTO> {
    private int minLikes;
    private int maxLikes;
    private List<PostDTO> backup;

    public FilterPostByLikesCommand(int minLikes, int maxLikes) {
        this.minLikes = minLikes;
        this.maxLikes = maxLikes;
    }

    @Override
    public List<PostDTO> execute(List<PostDTO> posts) {
        backup = new ArrayList<>(posts);

        return posts.stream()
                .filter(p -> {
                    int likes = p.getLikedBy().size();
                    return likes >= minLikes && likes <= maxLikes;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> undo() {
        return backup;
    }
}

