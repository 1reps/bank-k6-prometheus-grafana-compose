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

    public static Account create(String ownerName, BigDecimal initialBalance, String accountNumber) {
        Account account = new Account();
        account.accountNumber = accountNumber;
        account.ownerName = ownerName;
        account.balance = initialBalance == null ? BigDecimal.ZERO : initialBalance;
        account.createdAt = LocalDateTime.now();
        return account;
    }

    public void increaseBalance(BigDecimal amount) {
        if (isLessThanMin(amount)) {
            throw new IllegalArgumentException("입금 최소 금액 미달. 요청 금액=" + amount);
        }
        balance = balance.add(amount);
    }

    public void decreaseBalance(BigDecimal amount) {
        if (isBalanceLessThan(amount)) {
            throw new IllegalStateException("잔액이 부족합니다. 현재 보유 잔액=" + balance + ", 요청한 차감액=" + amount);
        }
        balance = balance.subtract(amount);
    }

    private boolean isLessThanMin(BigDecimal amount) {
        return amount.compareTo(BigDecimal.valueOf(1000L)) < 0;
    }

    private boolean isBalanceLessThan(BigDecimal amount) {
        return balance.compareTo(amount) < 0;
    }

}
