package com.example.msccspringtesting.domain.service;

import com.example.msccspringtesting.application.ports.output.AccountOutputPort;
import com.example.msccspringtesting.domain.exception.AccountNotFoundException;
import com.example.msccspringtesting.domain.exception.CustomerNotFoundException;
import com.example.msccspringtesting.domain.model.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountOutputPort accountOutputPort;
    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("An account object will be returned for a valid account number")
    void should_return_account_for_specific_id() {
        Account accountFromDB = new Account();
        accountFromDB.setAccountNumber("123456789");
        accountFromDB.setCurrentBalance(100.00);
        Mockito.when(this.accountOutputPort.findAccountByAccountId(ArgumentMatchers.anyString())).thenReturn(Optional.of(accountFromDB));
        Account account = this.accountService.getAccountByAccountNumber("123456789");
        Assertions.assertThat(account).isNotNull();
        Assertions.assertThat(account.getCurrentBalance()).isEqualTo(100.00);
        Mockito.verify(this.accountOutputPort, Mockito.times(1)).findAccountByAccountId(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("An exception will be thrown for an invalid account number")
    void should_throw_exception_for_non_existing_id() {
        Mockito.when(this.accountOutputPort.findAccountByAccountId(ArgumentMatchers.anyString())).thenThrow(new AccountNotFoundException("Account does not exist"));
        Assertions.assertThatThrownBy(() -> this.accountService.getAccountByAccountNumber("123")).isInstanceOf(AccountNotFoundException.class);
        Mockito.verify(this.accountOutputPort, Mockito.times(1)).findAccountByAccountId(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("Returns a list of accounts for specific customer")
    void should_return_account_list_for_specific_customer() {
        Account accountFromDB = new Account();
        accountFromDB.setAccountNumber("123456789");
        accountFromDB.setCurrentBalance(100.00);
        Mockito.when(this.accountOutputPort.getAccountByCustomerRefId(ArgumentMatchers.anyString())).thenReturn(List.of(accountFromDB));
        var accountList = this.accountService.getAccounts("1012");
        Assertions.assertThat(accountList).isNotNull().hasSize(1);
        Assertions.assertThat(accountList.get(0).getCurrentBalance()).isEqualTo(100.00);
        Mockito.verify(this.accountOutputPort, Mockito.times(1)).getAccountByCustomerRefId(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("An exception will be thrown for an invalid customer number")
    void should_throw_exception_for_invalid_customer() {
        Mockito.when(this.accountOutputPort.getAccountByCustomerRefId(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList());
        Assertions.assertThatThrownBy(() -> this.accountService.getAccounts("123")).isInstanceOf(CustomerNotFoundException.class);
        Mockito.verify(this.accountOutputPort, Mockito.times(1)).getAccountByCustomerRefId(ArgumentMatchers.anyString());
    }
}