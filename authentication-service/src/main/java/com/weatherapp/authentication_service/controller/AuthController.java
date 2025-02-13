package com.weatherapp.authentication_service.controller;

import com.weatherapp.authentication_service.dto.LoginRequest;
import com.weatherapp.authentication_service.dto.LoginResponse;
import com.weatherapp.authentication_service.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("signIn")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest){
        Authentication authenticate;
        try{
            authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        }catch (AuthenticationException ex){
            Map<String,Object> body = new HashMap<>();
            body.put("message",ex.getMessage());
            body.put("status",false);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        String token = jwtUtils.generateJwtToken(userDetails.getUsername());
//        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).toList();
        LoginResponse response = new LoginResponse();
        response.setToken(token);
//        response.setRoles(roles);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}
