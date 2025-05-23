package com.me.performance.domain.account;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);

    @Query(value = """
            SELECT a.balance 
            FROM Account a 
            WHERE a.id = :accountId
        """)
    Optional<Long> findBalanceById(Long accountId);

}
