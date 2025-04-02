package com.me.performance.domain.payment;

import com.me.performance.domain.account.Account;
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
    private final ReferenceNumberGenerator referenceNumberGenerator;

    public PaymentResponse read(Long paymentId) {
        return paymentRepository.findById(paymentId).map(PaymentResponse::from)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found paymentId=" + paymentId));
    }

    public List<PaymentResponse> readAll() {
        return paymentRepository.findAll().stream()
            .map(PaymentResponse::from)
            .toList();
    }

    public void deposit(Account account, BigDecimal amount) {
        Payment newPayment = Payment.createDeposit(
            referenceNumberGenerator.generateDepositReferenceNumber(),
            account,
            amount,
            null
        );
        paymentRepository.save(newPayment);
    }

    public void withdrawal(Account account, BigDecimal amount) {
        Payment newPayment = Payment.createWithdrawal(
            referenceNumberGenerator.generateWithdrawalReferenceNumber(),
            account,
            amount,
            null
        );
        paymentRepository.save(newPayment);
    }

}
