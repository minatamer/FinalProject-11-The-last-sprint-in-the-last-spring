package com.example.SearchApp.Clients;

import com.example.SearchApp.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {

    @GetMapping("/")
    List<UserDTO> getUsers(@RequestHeader(value = "Authorization", required = false) String token);

    @GetMapping("/token/{token}/userId")
    Map<String, UUID> getUserIdByToken(@PathVariable("token") String token);
}
