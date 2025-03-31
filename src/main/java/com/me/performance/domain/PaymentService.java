package com.me.performance.domain;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment read(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found id=" + id));
    }

    public List<Payment> readAll() {
        return paymentRepository.findAll().stream()
            .toList();
    }

}
