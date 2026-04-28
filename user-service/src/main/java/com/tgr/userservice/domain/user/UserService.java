package com.tgr.userservice.domain.user;

import com.tgr.userservice.domain.user.Models.User;
import com.tgr.userservice.infrastructure.persistence.UserRepository;
import com.tgr.userservice.infrastructure.web.DTOs;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<User> loadUser(Long id) {
        return userRepository.findById(id);
    }

    public Mono<List<User>> listUsers() {
        return userRepository.findAll().collectList();
    }

    public Mono<User> createUser(DTOs.UserRequest request) {
        return userRepository.findByEmail(request.email())
                .switchIfEmpty(Mono.defer(() -> {
                    var newUser = new User(null,
                            request.name(),
                            request.lastName(),
                            request.email(),
                            request.taxId(),
                            passwordEncoder.encode(request.password()),
                            Models.Status.ACTIVE,
                            request.role() != null ? request.role() : Models.Role.USER,
                            LocalDateTime.now(),
                            null);

                    return userRepository.save(newUser);
                }));
    }

    public Mono<User> updateUser(Long id, DTOs.UserRequest request) {
        return userRepository.findById(id)
                .map(user ->  ModelsUserBuilder.builder()
                        .name(request.name())
                        .lastName(request.lastName())
                        .build())
                .flatMap(userRepository::save);
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    public Mono<User> login(DTOs.LoginRequest request) {
        return userRepository.findByEmail(request.email())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid credentials")))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(request.password(), user.password())) {
                        return Mono.error(new IllegalArgumentException("Invalid credentials"));
                    }
                    if (user.status() != Models.Status.ACTIVE) {
                        return Mono.error(new IllegalArgumentException("Account is not active"));
                    }
                    return Mono.just(user);
                });
    }
}