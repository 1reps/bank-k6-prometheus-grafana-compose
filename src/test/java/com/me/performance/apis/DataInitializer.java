package com.me.performance.apis;

import com.me.performance.domain.account.AccountService;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataInitializer {

    @Autowired
    private AccountService accountService;

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private static final int THREAD_COUNT = 1000;

    @Test
    public void initData() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        logger.info("[DataInitializer.initData] threadCount={}", threadCount);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        logger.info("[DataInitializer.initData] start");

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    accountService.makeAccount("User" + index, BigDecimal.valueOf(10000));
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            boolean completed = latch.await(5, TimeUnit.MINUTES);
            executor.shutdown();
            logger.info("[DataInitializer.initData] completed={}", completed);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("[DataInitializer.initData] interrupted", e.getMessage());
            executor.shutdownNow();
            throw new RuntimeException(e);
        }
    }

}
