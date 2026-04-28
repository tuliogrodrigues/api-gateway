package com.tgr.userservice.infrastructure.web;

import com.tgr.userservice.domain.user.UserService;
import com.tgr.userservice.infrastructure.web.DTOs.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Mono<ResponseEntity<List<DTOs.UserResponse>>> listUsers() {
        return userService.listUsers()
                .map(users -> ResponseEntity.ok(users.stream()
                        .map(this::toResponse)
                        .toList()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DTOs.UserResponse>> getUser(@PathVariable Long id) {
        return userService.loadUser(id)
                .map(user -> ResponseEntity.ok(toResponse(user)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<DTOs.UserResponse>> createUser(@Valid @RequestBody DTOs.UserRequest request) {
        return userService.createUser(request)
                .map(user -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(toResponse(user)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DTOs.UserResponse>> updateUser(@PathVariable Long id, @Valid @RequestBody DTOs.UserRequest request) {
        return userService.updateUser(id, request)
                .map(user -> ResponseEntity.ok(toResponse(user)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                .thenReturn(ResponseEntity.noContent().build());
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
}