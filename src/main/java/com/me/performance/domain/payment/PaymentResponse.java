package com.me.performance.domain.payment;

import com.me.performance.domain.account.Account;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(Long id,
                              String referenceNumber,
                              Account sourceAccount,
                              Account targetAccount,
                              BigDecimal amount,
                              String description,
                              PaymentType type,
                              PaymentStatus status,
                              LocalDateTime createdAt

) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
            payment.getId(),
            payment.getReferenceNumber(),
            payment.getSourceAccount(),
            payment.getTargetAccount(),
            payment.getAmount(),
            payment.getDescription(),
            payment.getType(),
            payment.getStatus(),
            payment.getCreatedAt()
        );
    }

}
