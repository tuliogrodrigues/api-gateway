package com.tgr.userservice.domain.user;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    public Mono<Models.User> loadUser(final Long id) {
        return Mono.just(new Models.User(id, "John", "Doe"));
    }
}
