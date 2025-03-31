package com.me.performance.domain.payment;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentManager paymentManager;

    @GetMapping
    public List<PaymentResponse> getPayments() {
        return paymentService.readAll();
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse getPayment(@PathVariable Long paymentId) {
        return paymentService.read(paymentId);
    }

    @PostMapping("/deposit/{accountId}")
    public void deposit(@PathVariable Long accountId, BigDecimal amount) {
        paymentManager.depositByAccount(accountId, amount);
    }

    @PostMapping("/withdrawal/{accountId}")
    public void withdrawal(@PathVariable Long accountId, BigDecimal amount) {
        paymentManager.withdrawalByAccount(accountId, amount);
    }

}
