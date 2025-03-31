package com.me.performance.domain;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountResponse> getAccounts() {
        return accountService.readAll();
    }

    @GetMapping("/{id}")
    public AccountResponse getAccount(Long id) {
        return accountService.read(id);
    }

    @PostMapping
    public AccountResponse makeAccount(String ownerName) {
        return accountService.makeAccount(ownerName);
    }

}
