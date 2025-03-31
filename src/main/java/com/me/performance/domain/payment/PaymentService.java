package com.me.performance.domain.payment;

import com.me.performance.domain.account.Account;
import com.me.performance.domain.account.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final ReferenceNumberGenerator referenceNumberGenerator;

    public PaymentResponse read(Long id) {
        return paymentRepository.findById(id)
            .map(PaymentResponse::from)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found id=" + id));
    }

    public List<PaymentResponse> readAll() {
        return paymentRepository.findAll().stream()
            .map(PaymentResponse::from)
            .toList();
    }

    @Transactional
    public PaymentResponse deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다. id=" + accountId));

        account.increaseBalance(amount);

        Payment newDeposit = Payment.createDeposit(
            referenceNumberGenerator.generateDepositReferenceNumber(),
            account,
            amount,
            null
        );

        Payment savedPayment = paymentRepository.save(newDeposit);
        return PaymentResponse.from(savedPayment);
    }

    @Transactional
    public PaymentResponse withdrawal(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다. id=" + accountId));

        account.decreaseBalance(amount);

        Payment newWithdrawal = Payment.createWithdrawal(
            referenceNumberGenerator.generateWithdrawalReferenceNumber(),
            account,
            amount,
            null
        );

        Payment savedPayment = paymentRepository.save(newWithdrawal);
        return PaymentResponse.from(savedPayment);
    }

}
