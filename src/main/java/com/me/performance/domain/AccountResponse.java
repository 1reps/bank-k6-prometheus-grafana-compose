package com.me.performance.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(Long id,
                              String accountNumber,
                              String ownerName,
                              BigDecimal balance,
                              LocalDateTime createdAt
) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getId(), account.getAccountNumber(), account.getOwnerName(), account.getBalance(), account.getCreatedAt());
    }

}
