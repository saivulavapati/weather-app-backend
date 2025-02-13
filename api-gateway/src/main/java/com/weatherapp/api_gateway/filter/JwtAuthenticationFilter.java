package com.weatherapp.api_gateway.filter;

import com.weatherapp.api_gateway.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extract the token

            try {
                String username = jwtUtils.getSubjectFromJwtToken(token);

                // You might want to add more checks here (e.g., check if the user exists, etc.)
                if (username != null && jwtUtils.validateJwtToken(token, username)) {
                    // Add the username to the request headers for downstream services to use
                    ServerHttpRequest modifiedRequest = request.mutate()
                            .header("X-Authenticated-User", username) // Custom header
                            .build();

                    // Continue the filter chain
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                } else {
                    return handleUnauthorized(exchange.getResponse()); // Invalid token
                }
            } catch (Exception e) { // Catch JWT exceptions (e.g., expired, malformed)
                return handleUnauthorized(exchange.getResponse()); // Handle exceptions as unauthorized
            }
        } else {
            return handleUnauthorized(exchange.getResponse()); // No token or invalid format
        }
    }


    private Mono<Void> handleUnauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete(); // End the exchange
    }
}