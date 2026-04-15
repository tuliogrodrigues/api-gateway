package com.tgr.accountservice.domain.account;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {

    public Mono<Models.Account> loadAccount(final Long id){
        return Mono.just(
                new Models.Account(
                        id,
                        UUID.randomUUID(),
                        BigDecimal.valueOf(14340),
                        "EUR"));
    }
}
