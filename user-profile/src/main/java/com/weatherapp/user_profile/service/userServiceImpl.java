package com.weatherapp.user_profile.service;

import com.weatherapp.user_profile.entity.Role;
import com.weatherapp.user_profile.entity.User;
import com.weatherapp.user_profile.enums.RoleType;
import com.weatherapp.user_profile.repository.RoleRepository;
import com.weatherapp.user_profile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class userServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role roleUser = roleRepository.findByRoleName(RoleType.ROLE_USER).orElseThrow(() -> new RuntimeException("Role not Found"));
        user.getRoles().add(roleUser);
        userRepository.save(user);
        return "User Registered Successfully";
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
