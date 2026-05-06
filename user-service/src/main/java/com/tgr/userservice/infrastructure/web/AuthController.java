package com.tgr.userservice.infrastructure.web;

import com.tgr.userservice.domain.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class AuthController {

    private final UserService userService;
    private final WebClient keycloakClient;
    private final String clientId;
    private final String clientSecret;

    public AuthController(final UserService userService,
                          @org.springframework.beans.factory.annotation.Value("${KEYCLOAK_ISSUER_URI}") String issuerUri,
                          @org.springframework.beans.factory.annotation.Value("${KEYCLOAK_CLIENT_ID}") String clientId,
                          @org.springframework.beans.factory.annotation.Value("${KEYCLOAK_CLIENT_SECRET}") String clientSecret) {
        this.userService = userService;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        String tokenUrl = issuerUri.replace("/realms/", "/realms/") + "/protocol/openid-connect/token";
        this.keycloakClient = WebClient.builder()
                .baseUrl(tokenUrl)
                .build();
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<DTOs.LoginResponse>> login(@Valid @RequestBody DTOs.LoginRequest request) {
        String formBody = String.format("grant_type=password&client_id=%s&client_secret=%s&username=%s&password=%s",
                clientId, clientSecret, request.email(), request.password());

        return keycloakClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formBody)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    String token = (String) response.get("access_token");
                    Long expiresIn = ((Number) response.get("expires_in")).longValue();
                    return userService.findByEmail(request.email())
                            .map(user -> new DTOs.LoginResponse(
                                    token,
                                    "Bearer",
                                    expiresIn,
                                    new DTOs.UserResponse(user.id(), user.name(), user.lastName(),
                                            user.email(), user.taxId(), user.status().label(), user.role().value(),
                                            user.createdAt(), user.lastLogin())
                            ));
                })
                .map(ResponseEntity::ok);
    }
}