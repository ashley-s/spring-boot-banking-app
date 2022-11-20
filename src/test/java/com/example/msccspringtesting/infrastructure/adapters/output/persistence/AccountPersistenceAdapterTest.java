package com.example.msccspringtesting.infrastructure.adapters.output.persistence;

import com.example.msccspringtesting.domain.model.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

class AccountPersistenceAdapterTest extends AbstractPersistenceAdapterTest{

    @Autowired
    private AccountPersistenceAdapter accountPersistenceAdapterUnderTest;

    @Test
    void should_return_accountList_for_customer() {
        List<Account> accounts = this.accountPersistenceAdapterUnderTest.getAccountByCustomerRefId("1");
        Assertions.assertThat(accounts).hasSize(2);
    }

    @Test
    void should_return_account_for_account_number() {
        Optional<Account> account = this.accountPersistenceAdapterUnderTest.findAccountByAccountId("00123456789");
        Assertions.assertThat(account).isPresent();
    }
}