package com.me.performance.domain.account;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountGenerateNumber accountGenerateNumber;

    public AccountResponse read(Long id) {
        return accountRepository.findById(id)
            .map(AccountResponse::from)
            .orElseThrow(() -> new IllegalArgumentException("Account not found id=" + id));
    }

    public List<AccountResponse> readAll() {
        return accountRepository.findAll().stream()
            .map(AccountResponse::from)
            .toList();
    }

    @Transactional
    public AccountResponse makeAccount(String ownerName) {
        Account account = Account.create(ownerName, BigDecimal.ZERO, accountGenerateNumber);
        return AccountResponse.from(accountRepository.save(account));
    }

}
