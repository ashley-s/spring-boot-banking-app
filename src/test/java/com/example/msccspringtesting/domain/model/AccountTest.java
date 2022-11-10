package com.example.msccspringtesting.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    @DisplayName("This test will update the balance of the sender or receiver when an amount is being deducted")
    void should_Update_Balance_With_NegativeAmount() {
        Account account = new Account();
        account.setAccountNumber("1234");
        account.setCurrentBalance(100.00);
        account.updateBalance(-1.00);
        Assertions.assertThat(account.getCurrentBalance()).isEqualTo(99.00);
    }

    @Test
    @DisplayName("This test will update the balance of the sender or receiver when an amount is being added to the account")
    void should_Update_Balance_With_PositiveAmount() {
        Account account = new Account();
        account.setAccountNumber("1234");
        account.setCurrentBalance(100.00);
        account.updateBalance(1.00);
        Assertions.assertThat(account.getCurrentBalance()).isEqualTo(101.00);
    }
}