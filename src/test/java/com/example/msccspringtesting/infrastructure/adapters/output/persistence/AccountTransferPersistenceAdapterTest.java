package com.example.msccspringtesting.infrastructure.adapters.output.persistence;

import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.domain.model.AccountTransfer;
import com.example.msccspringtesting.domain.model.Customer;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.repository.AccountTransferRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class AccountTransferPersistenceAdapterTest extends AbstractPersistenceAdapterTest{

    @Autowired
    private AccountTransferPersistenceAdapter accountTransferPersistenceAdapterUnderTest;

    @Autowired
    private TransactionPersistenceAdapter transactionPersistenceAdapter;

    @Autowired
    private AccountTransferRepository accountTransferRepository;

    @Test
    void should_save_transfer_and_create_transaction() {
        AccountTransfer transfer = this.accountTransferPersistenceAdapterUnderTest.saveAccountTransfer(buildAccountTransfer());
        Assertions.assertThat(transfer).isNotNull();
        Assertions.assertThat(transfer.getDateCreated()).isNotNull();
        var transactionList = this.transactionPersistenceAdapter.getTransactionByAccountId("00123456789");
        Assertions.assertThat(transactionList).hasSize(2);
        Assertions.assertThat(this.accountTransferRepository.findAll()).hasSize(1);
    }

    private static AccountTransfer buildAccountTransfer() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setRefId("1022");
        customer.setActive(true);
        Account senderAccount = new Account();
        senderAccount.setId(1);
        senderAccount.setAccountNumber("00123456789");
        senderAccount.setCustomer(customer);
        senderAccount.setCurrentBalance(100.00);
        Account receiverAccount = new Account();
        receiverAccount.setId(2);
        receiverAccount.setAccountNumber("00777456789");
        receiverAccount.setCustomer(customer);
        receiverAccount.setCurrentBalance(100.00);
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setReceiverAccount(receiverAccount);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setAmount(1.00);
        accountTransfer.setTransferType("OWN");
        accountTransfer.setStatus("SUCCESS");
        accountTransfer.setReference(UUID.randomUUID().toString());
        return accountTransfer;
    }

}