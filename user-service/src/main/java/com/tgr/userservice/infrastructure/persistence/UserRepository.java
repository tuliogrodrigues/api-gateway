package com.tgr.userservice.infrastructure.persistence;

import com.tgr.userservice.domain.user.Models;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<Models.User, Long> {
    Mono<Models.User> findByEmail(String email);

}