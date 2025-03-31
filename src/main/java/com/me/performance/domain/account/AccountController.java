package com.me.performance.domain.account;

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

    @GetMapping("/{accountId}")
    public AccountResponse getAccount(Long accountId) {
        return accountService.read(accountId);
    }

    @PostMapping
    public AccountResponse makeAccount(String ownerName) {
        return accountService.makeAccount(ownerName);
    }

}
