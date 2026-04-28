package com.tgr.userservice.infrastructure.web;

import com.tgr.userservice.domain.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Internal API for service-to-service communication
 * Used by account-service to verify users exist and get user info
 */
@RestController
@RequestMapping("/internal/users")
public class InternalController {

    private final UserService userService;

    public InternalController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/verify")
    public Mono<ResponseEntity<UserVerification>> verifyUser(@PathVariable Long id) {
        return userService.loadUser(id)
                .map(user -> ResponseEntity.ok(new UserVerification(
                        user.id(),
                        true,
                        user.status().name(),
                        user.role().name()
                )))
                .defaultIfEmpty(ResponseEntity.ok(new UserVerification(id, false, null, null)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DTOs.UserResponse>> getUser(@PathVariable Long id) {
        return userService.loadUser(id)
                .map(user -> ResponseEntity.ok(toResponse(user)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private DTOs.UserResponse toResponse(com.tgr.userservice.domain.user.Models.User user) {
        return new DTOs.UserResponse(
                user.id(),
                user.name(),
                user.lastName(),
                user.email(),
                user.taxId(),
                user.status().label(),
                user.role().value(),
                user.createdAt(),
                user.lastLogin()
        );
    }

    public record UserVerification(
            Long userId,
            boolean exists,
            String status,
            String role) {}
}