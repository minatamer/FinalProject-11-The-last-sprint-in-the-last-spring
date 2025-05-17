package com.example.SearchApp.Command;

import com.example.SearchApp.model.UserDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterUserByDateCommand implements FilterCommand<UserDTO>{

    private LocalDate startDate;
    private  LocalDate endDate;
    private List<UserDTO> backup;

    public FilterUserByDateCommand(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public List<UserDTO> execute(List<UserDTO> users) {
        backup = new ArrayList<>(users);

        return users.stream()
                .filter(p -> {
                    LocalDate createdDate = p.getCreatedAt().toLocalDate();
                    return ( !createdDate.isBefore(startDate) ) &&  // on or after startDate
                            ( !createdDate.isAfter(endDate) );        // on or before endDate
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> undo() {
        return backup;
    }
}
