package com.example.SearchApp.Command;

import com.example.SearchApp.model.PostDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterPostBySharesCommand implements FilterCommand<PostDTO> {
    private int minShares;
    private int maxShares;
    private List<PostDTO> backup;

    public FilterPostBySharesCommand(int minShares, int maxShares) {
        this.minShares = minShares;
        this.maxShares = maxShares;
    }

    @Override
    public List<PostDTO> execute(List<PostDTO> posts) {
        backup = new ArrayList<>(posts);

        return posts.stream()
                .filter(p -> {
                    int shares = p.getSharedBy().size();
                    return shares >= minShares && shares <= maxShares;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> undo() {
        return backup;
    }
}

