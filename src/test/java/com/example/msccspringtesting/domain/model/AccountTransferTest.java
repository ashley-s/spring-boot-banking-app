package com.example.msccspringtesting.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountTransferTest {

    @Test
    @DisplayName("When Funds are not sufficient on account, an exception should be thrown for any transfer type")
    void should_ThrowException_WhenFundsIsNotSufficient() {
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setTransferType("OWN");
    }

}