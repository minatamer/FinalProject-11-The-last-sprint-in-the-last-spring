package com.example.SearchApp.Command;

import com.example.SearchApp.model.PostDTO;
import com.example.SearchApp.model.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class  FilterUserByAgeCommand implements FilterCommand<UserDTO> {
    private int minAge;
    private int maxAge;
    private List<UserDTO> backup;

    public FilterUserByAgeCommand(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    @Override
    public List<UserDTO> execute(List<UserDTO> users) {
        backup = new ArrayList<>(users);

        return users.stream()
                .filter(p -> {
                    int age = p.getAge();
                    return age >= minAge && age <= maxAge;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> undo() {
        return backup;
    }

}
