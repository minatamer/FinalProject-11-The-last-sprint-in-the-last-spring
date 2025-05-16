package com.example.SearchApp.Clients;

import com.example.SearchApp.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8080/user")
public interface UserClient {

    @GetMapping("/")
    List<UserDTO> getUsers(@RequestHeader(value = "Authorization", required = false) String token);


}
