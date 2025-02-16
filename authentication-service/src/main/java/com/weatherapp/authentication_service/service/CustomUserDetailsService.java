package com.weatherapp.authentication_service.service;

import com.weatherapp.authentication_service.client.UserProfileClient;
import com.weatherapp.authentication_service.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserProfileClient userProfileClient;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<Optional<User>> response = userProfileClient.getUserByUsername(username);
        Optional<User> user = response.getBody();
        if(user.isPresent()){
            return new CustomUserDetails(user.get().getUsername(),
                    user.get().getPassword());
        }
         throw new UsernameNotFoundException("User Not Found");
    }
}
