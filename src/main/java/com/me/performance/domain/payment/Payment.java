package com.me.performance.domain.payment;

import com.me.performance.domain.account.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Table(name = "payments")
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"sourceAccount", "targetAccount"})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String referenceNumber;

    // 자금이 출금되는 계좌(출급, 이체 시 사용)
    @ManyToOne
    private Account sourceAccount;

    // 자금이 입금되는 계좌(입금, 이체 시 사용)
    @ManyToOne
    private Account targetAccount;

    private BigDecimal amount;
    private String description;
    private PaymentType type; // DEPOSIT, WITHDRAWAL, TRANSFER
    private PaymentStatus status; // COMPLETED, FAILED
    private LocalDateTime createdAt;

    private Payment(String referenceNumber, Account sourceAccount, Account targetAccount, BigDecimal amount, String description, PaymentType type, PaymentStatus status) {
        this.referenceNumber = referenceNumber;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.status = status;
    }

    public Payment createDeposit(String referenceNumber, Account sourceAccount, BigDecimal amount, String description) {
        Payment payment = new Payment();
        payment.referenceNumber = referenceNumber;
        payment.sourceAccount = sourceAccount;
        payment.targetAccount = sourceAccount;
        payment.amount = amount;
        payment.description = description;
        payment.type = PaymentType.DEPOSIT;
        payment.status = PaymentStatus.COMPLETED;
        payment.createdAt = LocalDateTime.now();
        return payment;
    }

    public Payment createWithdrawal(String referenceNumber, Account sourceAccount, BigDecimal amount, String description) {
        Payment payment = new Payment();
        payment.referenceNumber = referenceNumber;
        payment.sourceAccount = sourceAccount;
        payment.targetAccount = sourceAccount;
        payment.amount = amount;
        payment.description = description;
        payment.type = PaymentType.WITHDRAWAL;
        payment.status = PaymentStatus.COMPLETED;
        payment.createdAt = LocalDateTime.now();
        return payment;
    }

}
