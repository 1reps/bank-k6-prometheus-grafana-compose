package com.me.performance.domain.account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "accounts")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private LocalDateTime createdAt;

    public static Account create(String ownerName, BigDecimal initialBalance, AccountGenerator accountGenerateNumber) {
        Account account = new Account();
        account.accountNumber = accountGenerateNumber.generateUniqNumber();
        account.ownerName = ownerName;
        account.balance = initialBalance;
        account.createdAt = LocalDateTime.now();
        return account;
    }

    public void increaseBalance(BigDecimal amount) {
        // 최소금액 미달 조건
        if (amount.compareTo(BigDecimal.valueOf(1000L)) <= 0) {
            throw new IllegalArgumentException("입금 최소 금액 미달. 요청 금액=" + amount);
        }
        balance = balance.add(amount);
    }

    public void decreaseBalance(BigDecimal amount) {
        // 잔고가 요청한 금액보다 작으면
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("잔액이 부족합니다. 현재 보유 잔액=" + balance + ", 요청한 차감액=" + amount);
        }
        balance = balance.subtract(amount);
    }

}
