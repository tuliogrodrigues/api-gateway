package com.tgr.userservice.infrastructure.persistence;

import com.tgr.userservice.domain.user.Models;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> userRepository.findByEmail("test@example.com")
                .switchIfEmpty(Mono.defer(() -> {
                    var user = new Models.User(
                            null,
                            "Test",
                            "User",
                            "test@example.com",
                            "123456789",
                            passwordEncoder.encode("password123"),
                            Models.Status.ACTIVE,
                            Models.Role.ADMIN,
                            LocalDateTime.now(),
                            null
                    );
                    return userRepository.save(user).then(Mono.empty());
                }))
                .block();
    }
}