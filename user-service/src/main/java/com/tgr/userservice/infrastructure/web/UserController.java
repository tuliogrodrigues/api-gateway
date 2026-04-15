package com.tgr.userservice.infrastructure.web;

import com.tgr.userservice.domain.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Mono<DTOs.User> create(@PathVariable Long id) {
        return userService.loadUser(id)
                .map(user -> new DTOs.User(user.id(), user.name(), user.lastName()));
    }
}
