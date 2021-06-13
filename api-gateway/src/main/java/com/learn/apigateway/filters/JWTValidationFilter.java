package com.learn.apigateway.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JWTValidationFilter implements GatewayFilter {

    @Value("${jwt.secret.key}")
    private String secretKey;
    public static final String AUTHORIZATION = "Authorization";
    private Logger logger = LoggerFactory.getLogger(JWTValidationFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        if (!request.getHeaders().containsKey(AUTHORIZATION)) {
            logger.error("Authorization not found");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        String authHeader = request.getHeaders().getOrEmpty(AUTHORIZATION).get(0);

        if (!authHeader.startsWith("Bearer ")) {
            logger.error("Bearer not found");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        String jwtToken = authHeader.substring(7);

        try {
            Claims claims = getClaims(jwtToken);
            String email = claims.getSubject();
            request.mutate().header("email", email);
        } catch (Exception e) {
            logger.error("exception in claims");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

    private Claims getClaims(String jwtToken) throws Exception {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
    }
}
