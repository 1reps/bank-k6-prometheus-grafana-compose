package com.me.performance.domain.account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountGenerator accountGenerator;

    @Transactional(readOnly = true)
    public AccountResponse read(Long accountId) {
        return accountRepository.findById(accountId)
            .map(AccountResponse::from)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계좌 입니다. accountId=" + accountId));
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> readAll() {
        return accountRepository.findAll().stream()
            .map(AccountResponse::from)
            .toList();
    }

    @Transactional
    public AccountResponse makeAccount(String ownerName, BigDecimal initialBalance) {
        Account account = Account.create(ownerName, initialBalance, accountGenerator.generateUniqNumber());

        return AccountResponse.from(accountRepository.save(account));
    }

    @Transactional
    public synchronized Account verifyDepositBalance(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계좌 입니다. accountId=" + accountId));

        account.increaseBalance(amount);
        return accountRepository.save(account);
    }

    @Transactional
    public synchronized Account verifyWithdrawalBalance(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계좌 입니다. accountId=" + accountId));

        account.decreaseBalance(amount);
        return accountRepository.save(account);
    }

    public Long readBalance(Long accountId) {
        return accountRepository.findBalanceById(accountId)
            .orElse(0L);
    }
}
