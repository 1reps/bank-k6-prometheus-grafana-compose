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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Table(name = "payments")
@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"sourceAccount", "targetAccount"})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String referenceNumber;

    @ManyToOne
    private Account sourceAccount; // 자금이 출금되는 계좌(출금, 이체 시 사용)

    @ManyToOne
    private Account targetAccount; // 자금이 입금되는 계좌(입금, 이체 시 사용)

    private BigDecimal amount;
    private String description;
    private PaymentType type;
    private PaymentStatus status;
    private LocalDateTime createdAt;

    public static Payment createDeposit(String referenceNumber, Account account, BigDecimal amount, String description) {
        Payment payment = new Payment();
        payment.referenceNumber = referenceNumber;
        payment.sourceAccount = account;
        payment.targetAccount = null;
        payment.amount = amount;
        payment.description = description;
        payment.type = PaymentType.DEPOSIT;
        payment.status = PaymentStatus.COMPLETED;
        payment.createdAt = LocalDateTime.now();
        return payment;
    }

    public static Payment createWithdrawal(String referenceNumber, Account account, BigDecimal amount, String description) {
        Payment payment = new Payment();
        payment.referenceNumber = referenceNumber;
        payment.sourceAccount = null;
        payment.targetAccount = account;
        payment.amount = amount;
        payment.description = description;
        payment.type = PaymentType.WITHDRAWAL;
        payment.status = PaymentStatus.COMPLETED;
        payment.createdAt = LocalDateTime.now();
        return payment;
    }

}
