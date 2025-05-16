package com.example.SearchApp.Command;


import com.example.SearchApp.model.PostDTO;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FilterPostByDateCommand implements FilterCommand<PostDTO> {
    private LocalDate startDate;
    private  LocalDate endDate;
    private List<PostDTO> backup;

    public FilterPostByDateCommand(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public List<PostDTO> execute(List<PostDTO> posts) {
        backup = new ArrayList<>(posts);

        return posts.stream()
                .filter(p -> {
                    LocalDate createdDate = p.getCreatedAt().toLocalDate();
                    return ( !createdDate.isBefore(startDate) ) &&  // on or after startDate
                            ( !createdDate.isAfter(endDate) );        // on or before endDate
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> undo() {
        return backup;
    }
}
