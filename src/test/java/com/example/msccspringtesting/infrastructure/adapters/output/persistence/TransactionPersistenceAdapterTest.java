package com.example.msccspringtesting.infrastructure.adapters.output.persistence;

import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.domain.model.Transaction;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionPersistenceAdapterTest extends AbstractPersistenceAdapterTest{

    @Autowired
    private TransactionPersistenceAdapter transactionPersistenceAdapterUnderTest;

    @Test
    @Order(1)
    @DisplayName("This test should return an empty list of transactions for an account number")
    void should_return_blank_list_for_account_number() {
        var transactionList = this.transactionPersistenceAdapterUnderTest.getTransactionByAccountId("001234567891458");
        Assertions.assertThat(transactionList).isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("This test should return a list of transactions for an account number")
    void should_return_transactionsList_for_account_number() {
        var transactionList = this.transactionPersistenceAdapterUnderTest.getTransactionByAccountId("00123456789");
        Assertions.assertThat(transactionList).hasSize(1);
        Assertions.assertThat(transactionList.get(0).getReference()).isEqualTo("TX123");
    }

    @Test
    @Order(3)
    @DisplayName("This test should save a transaction and returns the entity from the DB with a generated ID")
    void should_save_transaction_and_return_entity() {
        var transaction = this.transactionPersistenceAdapterUnderTest.saveTransaction(buildTransaction());
        Assertions.assertThat(transaction).isNotNull();
        Assertions.assertThat(transaction.getCompletionDate()).isNotNull();
        var transactionList = this.transactionPersistenceAdapterUnderTest.getTransactionByAccountId("00123456789");
        Assertions.assertThat(transactionList).hasSize(2);
    }

    private Transaction buildTransaction() {
        Account account = new Account();
        account.setId(1);
        account.setAccountNumber("00123456789");
        account.setCurrentBalance(100.00);
        Account senderAccount = new Account();
        senderAccount.setId(1);
        senderAccount.setAccountNumber("00123456789");
        senderAccount.setCurrentBalance(100.00);
        Account receiverAccount = new Account();
        receiverAccount.setId(1);
        receiverAccount.setAccountNumber("00456789425");
        receiverAccount.setCurrentBalance(200.00);
        Transaction transaction = new Transaction();
        transaction.setAccountOwner(account);
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);
        transaction.setReference("139f34e2-daf1-462c-966a-6660018c31c9");
        transaction.setAmount(2.00);
        return transaction;
    }
}