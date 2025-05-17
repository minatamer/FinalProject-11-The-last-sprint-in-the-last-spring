package com.example.SearchApp.Strategy;

import com.example.SearchApp.model.UserDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("userSearchStrategy")
public class UserSearchStrategy  implements SearchStrategy<UserDTO>{

    @Override
    public List<UserDTO> search(List<UserDTO> users, String query) {
        List<UserDTO> relevantUsers = new ArrayList<UserDTO>();

        for(UserDTO user : users)
        {
           if (user.getUsername().toLowerCase().trim().contains(query.toLowerCase().trim()) ||
               user.getEmail().toLowerCase().trim().contains(query.toLowerCase().trim()) ||
               user.getPhoneNumber().toLowerCase().trim().contains(query.toLowerCase().trim())
           )
               relevantUsers.add(user);
        }

        return relevantUsers;
    }
}
