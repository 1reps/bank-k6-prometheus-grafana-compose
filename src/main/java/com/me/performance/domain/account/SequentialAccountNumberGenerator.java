package com.me.performance.domain.account;

import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SequentialAccountNumberGenerator implements AccountGenerator {

    private final AtomicLong sequence = new AtomicLong(1000_0000L);
    private final AccountRepository accountRepository;

    private static final int PREFIX_ACCOUNT_NUMBER = 126;

    public String generateUniqNumber() {
        String candidateNumber = "";
        long next;
        boolean exists = true;
        while (exists) {
            next = sequence.incrementAndGet();
            candidateNumber = formatAccountNumber(next);
            exists = accountRepository.existsByAccountNumber(candidateNumber);
        }
        return candidateNumber;
    }

    private String formatAccountNumber(long number) {
        return String.format("%03d-%06d-%02d-%03d",
            PREFIX_ACCOUNT_NUMBER,
            number / 10000L % 10000,
            number % 10000,
            number % 10000
        );
    }

}
