package com.example.msccspringtesting.domain.model;

import com.example.msccspringtesting.domain.exception.FundsInsufficientException;
import com.example.msccspringtesting.domain.exception.TransferFundsNotPossibleException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountTransferTest {

    @Test
    @DisplayName("When Funds are not sufficient on account, an exception should be thrown for own account transfer")
    void should_ThrowException_WhenFundsIsNotSufficientForOwnAccountTransfer() {
        AccountTransfer ownAccountTransfer = buildOwnAccountTransferWith100MUR();
        ownAccountTransfer.setAmount(200.00);
        Assertions.assertThatThrownBy(ownAccountTransfer::isTransferFundsPossible).isInstanceOf(FundsInsufficientException.class).hasMessageContaining("Funds are insufficient on sender's Account");
    }

    @Test
    @DisplayName("When Funds are sufficient on account, no exception should be thrown for own account transfer")
    void should_Not_ThrowException_WhenFundsIsSufficientForOwnAccountTransfer() {
        AccountTransfer ownAccountTransfer = buildOwnAccountTransferWith100MUR();
        ownAccountTransfer.setAmount(50.00);
        Assertions.assertThatCode(ownAccountTransfer::isTransferFundsPossible).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("An Exception should be thrown for Own Account transfer if the sender and receiver accounts are not for the same customer")
    void should_ThrowException_ForOwnAccountTransferIfAccountsAreDifferent() {
        AccountTransfer ownAccountTransfer = buildOwnAccountTransferWith100MURWithDifferentAccount();
        ownAccountTransfer.setAmount(200.00);
        Assertions.assertThatThrownBy(ownAccountTransfer::isTransferFundsPossible).isInstanceOf(TransferFundsNotPossibleException.class).hasMessageContaining("Transfer of funds is not possible");
    }

    @Test
    @DisplayName("When Funds are not sufficient on account, an exception should be thrown for other account transfer")
    void should_ThrowException_WhenFundsIsNotSufficientForOtherAccountTransfer() {
        AccountTransfer ownAccountTransfer = buildOtherAccountTransferWith100MUR();
        ownAccountTransfer.setAmount(200.00);
        Assertions.assertThatThrownBy(ownAccountTransfer::isTransferFundsPossible).isInstanceOf(FundsInsufficientException.class).hasMessageContaining("Funds are insufficient on sender's Account");
    }

    @Test
    @DisplayName("When Funds are sufficient on account, no exception should be thrown for other account transfer")
    void should_Not_ThrowException_WhenFundsIsSufficientForOtherccountTransfer() {
        AccountTransfer ownAccountTransfer = buildOwnAccountTransferWith100MUR();
        ownAccountTransfer.setAmount(50.00);
        Assertions.assertThatCode(ownAccountTransfer::isTransferFundsPossible).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("An Exception should be thrown for Other Account transfer if the sender and receiver accounts are for the same customer")
    void should_ThrowException_ForOtherAccountTransferIfAccountsAreSame() {
        AccountTransfer ownAccountTransfer = buildOtherAccountTransferWith100MURWithSameCustomer();
        ownAccountTransfer.setAmount(200.00);
        Assertions.assertThatThrownBy(ownAccountTransfer::isTransferFundsPossible).isInstanceOf(TransferFundsNotPossibleException.class).hasMessageContaining("Transfer of funds is not possible");
    }

    private static AccountTransfer buildOwnAccountTransferWith100MUR() {
        Customer customer = new Customer();
        customer.setRefId("123");
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setTransferType("OWN");
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("123456");
        senderAccount.setCurrentBalance(100.00);
        senderAccount.setCustomer(customer);
        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("456789");
        receiverAccount.setCustomer(customer);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setReceiverAccount(receiverAccount);
        return accountTransfer;
    }

    private static AccountTransfer buildOtherAccountTransferWith100MUR() {
        Customer senderCustomer = new Customer();
        senderCustomer.setRefId("123");
        Customer anotherCustomer = new Customer();
        anotherCustomer.setRefId("4567");
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setTransferType("OTHER");
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("123456");
        senderAccount.setCurrentBalance(100.00);
        senderAccount.setCustomer(senderCustomer);
        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("456789");
        receiverAccount.setCustomer(anotherCustomer);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setReceiverAccount(receiverAccount);
        return accountTransfer;
    }

    private static AccountTransfer buildOtherAccountTransferWith100MURWithSameCustomer() {
        Customer senderCustomer = new Customer();
        senderCustomer.setRefId("123");
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setTransferType("OTHER");
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("123456");
        senderAccount.setCurrentBalance(100.00);
        senderAccount.setCustomer(senderCustomer);
        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("456789");
        receiverAccount.setCustomer(senderCustomer);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setReceiverAccount(receiverAccount);
        return accountTransfer;
    }

    private static AccountTransfer buildOwnAccountTransferWith100MURWithDifferentAccount() {
        Customer customer = new Customer();
        customer.setRefId("123");
        Customer anotherCustomer = new Customer();
        anotherCustomer.setRefId("4567");
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setTransferType("OWN");
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("123456");
        senderAccount.setCurrentBalance(100.00);
        senderAccount.setCustomer(customer);
        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("456789");
        receiverAccount.setCustomer(anotherCustomer);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setReceiverAccount(receiverAccount);
        return accountTransfer;
    }

}