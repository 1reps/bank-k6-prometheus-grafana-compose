package com.me.performance.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import com.me.performance.domain.Account;
import com.me.performance.domain.AccountRepository;
import com.me.performance.domain.AccountService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AccountConcurrencyTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("10000개 계좌 동시 생성 테스트")
    void create1000AccountsConcurrently() throws InterruptedException {
        // given
        int numberOfAccounts = 10000;
        CountDownLatch latch;
        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            latch = new CountDownLatch(numberOfAccounts);

            // when
            for (int i = 0; i < numberOfAccounts; i++) {
                final String ownerName = "User" + i;

                executorService.submit(() -> {
                    try {
                        accountService.makeAccount(ownerName);
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        // 모든 작업이 완료될 때까지 대기 (최대 30초)
        boolean completed = latch.await(30, TimeUnit.SECONDS);

        // then
        assertThat(completed).isTrue(); // 시간 내에 모든 작업이 완료되었는지 확인

        long createdAccounts = accountRepository.count();
        assertThat(createdAccounts).isEqualTo(numberOfAccounts);

        // 계좌번호 중복 여부 확인
        List<String> accountNumbers = accountRepository.findAll().stream()
            .map(Account::getAccountNumber)
            .toList();

        Set<String> uniqueAccountNumbers = new HashSet<>(accountNumbers);
        assertThat(uniqueAccountNumbers.size()).isEqualTo(numberOfAccounts);
    }
}