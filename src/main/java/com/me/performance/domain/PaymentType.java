package com.me.performance.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType {

    DEPOSIT("입금"), 
    WITHDRAWAL("출금"), 
    TRANSFER("이체");

    private final String description;

}
