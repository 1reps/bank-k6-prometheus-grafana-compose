package com.me.performance.domain.payment;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReferenceNumberGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int MAX_RETRY_ATTEMPTS = 5;

    private final PaymentRepository paymentRepository;

    private AtomicLong sequence = new AtomicLong(0);
    private String nowDateStr;

    @PostConstruct
    public void init() {
        nowDateStr = LocalDate.now().format(DATE_FORMATTER);

        log.info("[ReferenceNumberGenerator.init] nowDateStr={}", nowDateStr);

        // 데이터베이스에서 가장 최근 일련번호 조회
        Long lastSequence = paymentRepository.findMaxSequenceForToday(nowDateStr);

        // 조회된 값이 없으면 0부터 시작, 있으면 그 다음 값부터 시작
        sequence = new AtomicLong(lastSequence != null ? lastSequence : 0);
    }

    public String generateDepositReferenceNumber() {
        return generateReferenceNumber("DEP");
    }

    public String generateWithdrawalReferenceNumber() {
        return generateReferenceNumber("WIT");
    }

    private String generateReferenceNumber(String prefix) {
        for (int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++) {
            long nextSeq = sequence.incrementAndGet();
            String newReferenceNumber = String.format("%s-%s-%06d", prefix, nowDateStr, nextSeq);

            // 데이터베이스 존재 여부 한 번만 확인
            if (!paymentRepository.existsByReferenceNumber(newReferenceNumber)) {
                return newReferenceNumber;
            }
        }

        // 최대 재시도 횟수 초과 시 예외 발생
        throw new IllegalStateException("최대 재시도 횟수 초과=" + MAX_RETRY_ATTEMPTS);
    }

}
