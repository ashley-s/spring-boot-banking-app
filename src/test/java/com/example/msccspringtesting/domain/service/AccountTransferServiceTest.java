package com.example.msccspringtesting.domain.service;

import com.example.msccspringtesting.application.ports.output.AccountTransferEventPublisher;
import com.example.msccspringtesting.application.ports.output.AccountTransferOutputPort;
import com.example.msccspringtesting.domain.event.AccountTransferEvent;
import com.example.msccspringtesting.domain.exception.FundsInsufficientException;
import com.example.msccspringtesting.domain.exception.TransferFundsNotPossibleException;
import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.domain.model.AccountTransfer;
import com.example.msccspringtesting.domain.model.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountTransferServiceTest {

    @Mock
    private AccountService accountService;
    @Mock
    private AccountTransferOutputPort accountTransferOutputPort;
    @Mock
    private AccountTransferEventPublisher accountTransferEventPublisher;
    @InjectMocks
    private AccountTransferService accountTransferService;
    @Captor
    private ArgumentCaptor<AccountTransfer> accountTransferArgumentCaptor;
    @Captor
    private ArgumentCaptor<AccountTransferEvent> accountTransferEventArgumentCaptor;

    @Test
    @DisplayName("This test should create a successful transfer with funds being deducted from the sender account and funds being credited to the receiver account")
    void should_return_a_successful_account_transfer() {
        Mockito.when(this.accountService.getAccountByAccountId(ArgumentMatchers.anyString())).thenReturn(getSenderAccount()).thenReturn(getReceiverAccount());
        Mockito.when(this.accountTransferOutputPort.saveAccountTransfer(ArgumentMatchers.any(AccountTransfer.class))).thenReturn(getSuccessfulAccountTransfer());
        var accountTransfer = this.accountTransferService.createAccountTransfer(getAccountTransfer());
        Assertions.assertThat(accountTransfer).isNotNull();
        Assertions.assertThat(accountTransfer.getStatus()).isEqualTo("SUCCESS");
        Mockito.verify(this.accountService, Mockito.times(2)).getAccountByAccountId(ArgumentMatchers.anyString());
        Mockito.verify(this.accountTransferOutputPort, Mockito.times(1)).saveAccountTransfer(this.accountTransferArgumentCaptor.capture());
        var accountTransferModel = this.accountTransferArgumentCaptor.getValue();
        var senderAccount = accountTransferModel.getSenderAccount();
        var receiverAccount = accountTransferModel.getReceiverAccount();
        Assertions.assertThat(senderAccount.getCurrentBalance()).isEqualTo(99.00);
        Assertions.assertThat(receiverAccount.getCurrentBalance()).isEqualTo(101.00);
        Mockito.verify(this.accountTransferEventPublisher, Mockito.times(1)).publishSuccessfulTransferEvent(this.accountTransferEventArgumentCaptor.capture());
        var accountTransferEvent = this.accountTransferEventArgumentCaptor.getValue();
        Assertions.assertThat(accountTransferEvent.getReference()).isEqualTo("139f34e2-daf1-462c-966a-6660018c31c9");
    }

    @Nested
    class FailedAccountTransfers {

        @Test
        @DisplayName("This test should not create a successful transfer for insufficient funds, with funds not being deducted from the sender account and funds not being credited to the receiver account")
        void should_throw_exception_for_insufficient_funds() {
            Mockito.when(accountService.getAccountByAccountId(ArgumentMatchers.anyString())).thenReturn(getSenderAccount()).thenReturn(getReceiverAccount());
            AccountTransfer accountTransferOf200 = getAccountTransferOf200();
            Assertions.assertThatThrownBy(() -> accountTransferService.createAccountTransfer(accountTransferOf200)).isInstanceOf(FundsInsufficientException.class);
        }

        @Test
        @DisplayName("This test should not create a successful transfer for invalid account transfer, with funds not being deducted from the sender account and funds not being credited to the receiver account")
        void should_throw_exception_for_invalid_transfer() {
            Mockito.when(accountService.getAccountByAccountId(ArgumentMatchers.anyString())).thenReturn(getSenderAccount()).thenReturn(getReceiverAccountInvalid());
            AccountTransfer accountTransferOf200 = getAccountTransferOf200();
            Assertions.assertThatThrownBy(() -> accountTransferService.createAccountTransfer(accountTransferOf200)).isInstanceOf(TransferFundsNotPossibleException.class);
        }

        @AfterEach
        void verifyMocks() {
            Mockito.verify(accountService, Mockito.times(2)).getAccountByAccountId(ArgumentMatchers.anyString());
            Mockito.verify(accountTransferOutputPort, Mockito.times(1)).saveAccountTransfer(accountTransferArgumentCaptor.capture());
            var accountTransferModel = accountTransferArgumentCaptor.getValue();
            var senderAccount = accountTransferModel.getSenderAccount();
            var receiverAccount = accountTransferModel.getReceiverAccount();
            Assertions.assertThat(senderAccount.getCurrentBalance()).isEqualTo(100.00);
            Assertions.assertThat(receiverAccount.getCurrentBalance()).isEqualTo(100.00);
            Assertions.assertThat(accountTransferModel.getStatus()).isEqualTo("FAILURE");
            Mockito.verify(accountTransferEventPublisher, Mockito.times(0)).publishSuccessfulTransferEvent(ArgumentMatchers.any(AccountTransferEvent.class));
        }
    }

    @Test
    void should_return_exception_for_internalserver_errors() {

    }



    private static Account getSenderAccount() {
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setCurrentBalance(100.00);
        Customer customer = new Customer();
        customer.setRefId("1012");
        account.setCustomer(customer);
        return account;
    }
    private static Account getReceiverAccount() {
        Account account = new Account();
        account.setAccountNumber("46789123");
        account.setCurrentBalance(100.00);
        Customer customer = new Customer();
        customer.setRefId("1012");
        account.setCustomer(customer);
        return account;
    }
    private static Account getReceiverAccountInvalid() {
        Account account = new Account();
        account.setAccountNumber("46789123");
        account.setCurrentBalance(100.00);
        Customer customer = new Customer();
        customer.setRefId("1013");
        account.setCustomer(customer);
        return account;
    }
    private static AccountTransfer getAccountTransfer() {
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("12345789");
        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("456789123");
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setReceiverAccount(receiverAccount);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setAmount(1.00);
        accountTransfer.setTransferType("OWN");
        return accountTransfer;
    }
    private static AccountTransfer getAccountTransferOf200() {
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("12345789");
        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("456789123");
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setReceiverAccount(receiverAccount);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setAmount(200.00);
        accountTransfer.setTransferType("OWN");
        return accountTransfer;
    }
    private static AccountTransfer getSuccessfulAccountTransfer() {
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("12345789");
        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("456789123");
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setReceiverAccount(receiverAccount);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setAmount(1.00);
        accountTransfer.setTransferType("OWN");
        accountTransfer.setStatus("SUCCESS");
        accountTransfer.setReference("139f34e2-daf1-462c-966a-6660018c31c9");
        return accountTransfer;
    }
}