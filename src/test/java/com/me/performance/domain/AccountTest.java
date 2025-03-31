package com.me.performance.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AccountTest {

    @Autowired
    private AccountGenerateNumber accountGenerateNumber;

    @Autowired
    private AccountRepository accountRepository;

    private String ownerName;
    private BigDecimal initialBalance;
    private String accountNumber;

    @BeforeEach
    void setUp() {
        ownerName = "홍길동";
        initialBalance = new BigDecimal("10000.00");
        accountNumber = "126-1234-5678";
    }

    @Test
    @DisplayName("계좌 생성 테스트")
    void testCreateAccount() {
        // given
        Account account = Account.create(ownerName, initialBalance, accountGenerateNumber);

        // then
        Account savedAccount = accountRepository.save(account);

        // when
        assertThat(ownerName).isEqualTo(savedAccount.getOwnerName());
    }

    @Test
    @DisplayName("계좌 잔액 증가 테스트")
    void testIncreaseBalance() {
        // given
        Account account = Account.create(ownerName, initialBalance, accountGenerateNumber);
        BigDecimal depositAmount = new BigDecimal("5000.00");
        BigDecimal expectedBalance = initialBalance.add(depositAmount);

        // when
        account.increaseBalance(depositAmount);

        // then
        assertThat(expectedBalance).isEqualTo(account.getBalance());
    }

    @Test
    @DisplayName("계좌 잔액 감소 테스트 - 정상 출금")
    void testDecreaseBalance() {
        // given
        Account account = Account.create(ownerName, initialBalance, accountGenerateNumber);
        BigDecimal withdrawAmount = new BigDecimal("5000.00");
        BigDecimal expectedBalance = initialBalance.subtract(withdrawAmount);

        // when
        account.decreaseBalance(withdrawAmount);

        // then
        assertThat(expectedBalance).isEqualTo(account.getBalance());
    }

    @Test
    @DisplayName("계좌 잔액 감소 테스트 - 잔액 부족 예외 발생")
    void testDecreaseBalanceWithInsufficientFunds() {
        // given
        Account account = Account.create(ownerName, initialBalance, accountGenerateNumber);
        BigDecimal withdrawAmount = new BigDecimal("15000.00"); // 초기 잔액보다 큰 금액

        // when & then
        assertThatThrownBy(() -> account.decreaseBalance(withdrawAmount))
            .isInstanceOfAny(IllegalStateException.class)
            .hasMessage("잔액이 부족합니다. 현재 보유 잔액=" + initialBalance + ", 요청한 차감액=" + withdrawAmount);
    }

    @Test
    @DisplayName("0원 출금 테스트")
    void testZeroAmountWithdrawal() {
        // given
        Account account = Account.create(ownerName, initialBalance, accountGenerateNumber);
        BigDecimal withdrawAmount = BigDecimal.ZERO;

        // when
        account.decreaseBalance(withdrawAmount);

        // then
        assertThat(initialBalance).isEqualTo(account.getBalance());
    }
}