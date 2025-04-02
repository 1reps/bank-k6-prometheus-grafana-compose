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
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountGenerator accountGenerator;

    public AccountResponse read(Long accountId) {
        return accountRepository.findById(accountId)
            .map(AccountResponse::from)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계좌 입니다. accountId=" + accountId));
    }

    public List<AccountResponse> readAll() {
        return accountRepository.findAll().stream()
            .map(AccountResponse::from)
            .toList();
    }

    public Long readBalance(Long accountId) {
        return accountRepository.findBalanceById(accountId)
            .orElse(0L);
    }

    @Transactional
    public AccountResponse makeAccount(String ownerName, BigDecimal initialBalance) {
        Account account = Account.create(ownerName, initialBalance, accountGenerator.generateUniqNumber());
        Account savedAccount = accountRepository.save(account);
        return AccountResponse.from(savedAccount);
    }

    public Account verifyDepositBalance(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findByIdWithPessimisticLock(accountId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계좌 입니다. accountId=" + accountId));

        account.increaseBalance(amount);
        return accountRepository.save(account);
    }

    public Account verifyWithdrawalBalance(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findByIdWithPessimisticLock(accountId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계좌 입니다. accountId=" + accountId));

        account.decreaseBalance(amount);
        return accountRepository.save(account);
    }
}
