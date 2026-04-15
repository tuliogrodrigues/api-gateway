package com.tgr.accountservice.infrastructure.web;

import com.tgr.accountservice.domain.account.AccountService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public Mono<DTOs.Account> create(@PathVariable Long id) {
        return accountService.loadAccount(id)
                .map(account -> new DTOs.Account(account.balance(), account.currency()));
    }
}
