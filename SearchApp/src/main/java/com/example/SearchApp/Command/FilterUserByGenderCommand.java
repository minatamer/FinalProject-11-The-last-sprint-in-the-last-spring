package com.example.SearchApp.Command;

import com.example.SearchApp.model.UserDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class FilterUserByGenderCommand implements FilterCommand<UserDTO> {

    private final String gender;
    private List<UserDTO> backup;

    public FilterUserByGenderCommand(String gender) {
        this.gender = gender.trim().toLowerCase(); // normalize for comparison
    }

    @Override
    public List<UserDTO> execute(List<UserDTO> users) {
        backup = new ArrayList<>(users);

        return users.stream()
                .filter(u -> u.getGender() != null && u.getGender().trim().toLowerCase().equals(gender))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> undo() {
        return backup;
    }
}
