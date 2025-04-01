// 이 파일은 더 이상 사용되지 않습니다.
// API가 BigDecimal을 직접 처리하도록 변경되었습니다.
// 향후 확장 시 다시 사용할 수 있도록 유지합니다.

package com.me.performance.domain.payment;

import java.math.BigDecimal;

public record PaymentRequest(BigDecimal amount, String description) {
}
