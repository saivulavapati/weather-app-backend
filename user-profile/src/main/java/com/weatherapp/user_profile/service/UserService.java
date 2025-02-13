package com.weatherapp.user_profile.service;

import com.weatherapp.user_profile.entity.User;

import java.util.Optional;

public interface UserService {
    String register(User user);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);
}
