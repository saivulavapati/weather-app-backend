package com.weatherapp.api_gateway.filter;

import com.weatherapp.api_gateway.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.WebUtils;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    @Autowired
    private JwtUtils jwtUtils;

    private String jwtCookie;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Extract JWT from cookies
        HttpCookie jwtCookie = request.getCookies().getFirst("jwtCookie"); // Assuming cookie name is "jwt"

        if (jwtCookie != null) {
            String token = jwtCookie.getValue(); // Extract the token

            try {
                String username = jwtUtils.getSubjectFromJwtToken(token);

                if (username != null && jwtUtils.validateJwtToken(token, username)) {
                    // Add the username to the request headers for downstream services
                    ServerHttpRequest modifiedRequest = request.mutate()
                            .header("X-Authenticated-User", username) // Custom header
                            .build();

                    // Continue the filter chain
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                } else {
                    return handleUnauthorized(exchange.getResponse()); // Invalid token
                }
            } catch (Exception e) {
                return handleUnauthorized(exchange.getResponse()); // Handle JWT exceptions
            }
        } else {
            return handleUnauthorized(exchange.getResponse()); // No JWT in cookies
        }
    }



    private Mono<Void> handleUnauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete(); // End the exchange
    }
}