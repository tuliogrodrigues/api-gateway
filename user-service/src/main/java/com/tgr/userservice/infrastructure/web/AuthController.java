package com.tgr.userservice.infrastructure.web;

import com.tgr.userservice.domain.user.UserService;
import com.tgr.userservice.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(final UserService userService, final JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<DTOs.LoginResponse>> login(@Valid @RequestBody DTOs.LoginRequest request) {
        return userService.login(request)
                .map(user -> {
                    String token = jwtService.generateToken(user.email(), user.id(), user.role().value());
                    return new DTOs.LoginResponse(
                            token,
                            "Bearer",
                            3600L,
                            new DTOs.UserResponse(user.id(),
                                    user.name(),
                                    user.lastName(),
                                    user.email(),
                                    user.taxId(),
                                    user.status().label(),
                                    user.role().value(),
                                    user.createdAt(),
                                    user.lastLogin())
                    );
                })
                .map(ResponseEntity::ok);
    }
}