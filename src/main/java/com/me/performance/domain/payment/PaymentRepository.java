package com.me.performance.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByReferenceNumber(String referenceNumber);

    @Query(value = """
        SELECT MAX(CAST(SUBSTRING(p.referenceNumber, -6) AS long))
        FROM Payment p
        WHERE p.referenceNumber LIKE :dateStr%
        """)
    Long findMaxSequenceForToday(@Param("dateStr") String dateStr);
}
