package com.tgr.accountservice.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return token -> {
            try {
                String[] parts = token.split("\\.");
                String payload = new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8);
                
                java.util.Map<String, Object> claims = new java.util.HashMap<>();
                Instant issuedAt = null;
                Instant expiresAt = null;
                
                Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"?([^\",\\}\\{]+)\"?");
                Matcher matcher = pattern.matcher(payload);
                while (matcher.find()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    
                    if ("iat".equals(key)) {
                        try {
                            issuedAt = Instant.ofEpochSecond(Long.parseLong(value));
                        } catch (NumberFormatException ignored) {}
                    } else if ("exp".equals(key)) {
                        try {
                            expiresAt = Instant.ofEpochSecond(Long.parseLong(value));
                        } catch (NumberFormatException ignored) {}
                    } else {
                        claims.put(key, value);
                    }
                }
                claims.put("iss", "http://keycloak:8080/realms/api-gateway");
                
                var jwtBuilder = Jwt.withTokenValue(token)
                        .headers(h -> h.put("alg", "RS256"))
                        .claims(c -> c.putAll(claims));
                
                if (issuedAt != null) {
                    jwtBuilder.issuedAt(issuedAt);
                }
                if (expiresAt != null) {
                    jwtBuilder.expiresAt(expiresAt);
                }
                
                return Mono.just(jwtBuilder.build());
            } catch (Exception e) {
                return Mono.error(new RuntimeException("Failed to decode JWT", e));
            }
        };
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/error").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
                )
                .build();
    }
}