package com.me.performance.domain.payment;

import com.me.performance.domain.account.Account;
import com.me.performance.domain.account.AccountService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Account, Payment 책임을 분리하기 위한 Facade class
 */
@Component
@RequiredArgsConstructor
public class PaymentManager {

    private final AccountService accountService;
    private final PaymentService paymentService;

    @Transactional
    public void depositByAccount(Long accountId, BigDecimal amount) {
        Account account = accountService.verifyDepositBalance(accountId, amount);

        paymentService.deposit(account, amount);
    }

    @Transactional
    public void withdrawalByAccount(Long accountId, BigDecimal amount) {
        Account account = accountService.verifyWithdrawalBalance(accountId, amount);

        paymentService.withdrawal(account, amount);
    }

}
