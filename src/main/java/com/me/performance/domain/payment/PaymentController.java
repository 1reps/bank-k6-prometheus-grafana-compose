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

    @GetMapping
    public List<PaymentResponse> getPayments() {
        return paymentService.readAll();
    }

    @GetMapping("/{id}")
    public PaymentResponse getPayment(@PathVariable Long id) {
        return paymentService.read(id);
    }

    @PostMapping("/deposit/{accountId}")
    public PaymentResponse deposit(@PathVariable Long accountId, BigDecimal amount) {
        return paymentService.deposit(accountId, amount);
    }

    @PostMapping("/withdrawal/{accountId}")
    public PaymentResponse withdrawal(@PathVariable Long accountId, BigDecimal amount) {
        return paymentService.withdrawal(accountId, amount);
    }

}
