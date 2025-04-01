package com.me.performance.domain.account;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public AccountResponse getAccount(@PathVariable Long accountId) {
        return accountService.read(accountId);
    }

    @GetMapping("/{accountId}/balance")
    public Long getBalance(@PathVariable Long accountId) {
        return accountService.readBalance(accountId);
    }

    @PostMapping
    public AccountResponse makeAccount(String ownerName, BigDecimal initialBalance) {
        return accountService.makeAccount(ownerName, initialBalance);
    }

}
