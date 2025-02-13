package com.weatherapp.authentication_service.client;


import com.weatherapp.authentication_service.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "user-profile-service",url = "http://localhost:8081")
public interface UserProfileClient {

    @GetMapping("/users/username")
    public ResponseEntity<Optional<User>> getUserByUsername(@RequestParam String username);
}
