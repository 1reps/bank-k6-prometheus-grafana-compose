package com.me.performance.domain.payment;

import com.me.performance.domain.account.Account;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReferenceNumberGenerator referenceNumberGenerator;

    @Transactional(readOnly = true)
    public PaymentResponse read(Long paymentId) {
        return paymentRepository.findById(paymentId)
            .map(PaymentResponse::from)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found paymentId=" + paymentId));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> readAll() {
        return paymentRepository.findAll().stream()
            .map(PaymentResponse::from)
            .toList();
    }

    public void deposit(Account account, BigDecimal amount) {
        Payment newDeposit = Payment.createDeposit(
            referenceNumberGenerator.generateDepositReferenceNumber(),
            account,
            amount,
            null
        );

        PaymentResponse.from(paymentRepository.save(newDeposit));
    }

    public void withdrawal(Account account, BigDecimal amount) {
        Payment newWithdrawal = Payment.createWithdrawal(
            referenceNumberGenerator.generateWithdrawalReferenceNumber(),
            account,
            amount,
            null
        );

        PaymentResponse.from(paymentRepository.save(newWithdrawal));
    }

}
