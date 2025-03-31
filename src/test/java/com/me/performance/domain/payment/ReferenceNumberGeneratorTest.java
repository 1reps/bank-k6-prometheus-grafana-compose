package com.me.performance.domain.payment;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReferenceNumberGeneratorTest {

    @Autowired
    private ReferenceNumberGenerator referenceNumberGenerator;

    @Test
    void testReferenceNumberGeneration() {
        Set<String> generatedNumbers = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            String refNumber = referenceNumberGenerator.generateDepositReferenceNumber();
            assertThat(generatedNumbers).doesNotContain(refNumber);
            generatedNumbers.add(refNumber);
        }
    }

    @Test
    void testConcurrentReferenceNumberGeneration() throws InterruptedException {
        int threadCount = 100;
        Set<String> generatedNumbers;
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            generatedNumbers = Collections.synchronizedSet(new HashSet<>());

            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        generatedNumbers.add(referenceNumberGenerator.generateDepositReferenceNumber());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            executorService.shutdown();
        }

        assertThat(generatedNumbers).hasSize(threadCount);
    }

}