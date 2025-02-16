package com.weatherapp.api_gateway.configuration;

import com.weatherapp.api_gateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("user-profile-service",
                        r->r.path("/users/**")
                                .filters(f->f.filter(jwtAuthenticationFilter))
                                .uri("lb://USER-PROFILE-SERVICE"))
                .route("weather-service",
                        r-> r.path("/weather/**","/forecast/**")
                                .filters(f->f.filter(jwtAuthenticationFilter))
                                .uri("lb://WEATHER-SERVICE"))
                .route("authentication-service",
                        r->r.path("/auth/**").uri("lb://AUTHENTICATION-SERVICE"))
                .build();

    }
}
