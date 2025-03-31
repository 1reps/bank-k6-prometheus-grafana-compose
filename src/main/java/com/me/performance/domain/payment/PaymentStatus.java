package com.me.performance.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

    COMPLETED("완료"),
    FAILED("실패"),
    PENDING("대기");

    private final String description;

}
