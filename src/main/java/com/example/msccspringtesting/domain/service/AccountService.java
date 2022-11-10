package com.example.msccspringtesting.domain.service;

import com.example.msccspringtesting.application.ports.input.AccountUseCase;
import com.example.msccspringtesting.application.ports.output.AccountOutputPort;
import com.example.msccspringtesting.domain.exception.AccountNotFoundException;
import com.example.msccspringtesting.domain.model.Account;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AccountService implements AccountUseCase {

    private final AccountOutputPort accountOutputPort;

    public Account getAccountByAccountId(String accountId) {
        return this.accountOutputPort.findAccountByAccountId(accountId).orElseThrow(() -> new AccountNotFoundException("Account does not exist"));
    }

    @Override
    public List<Account> getAccounts(String customerId) {
        return this.accountOutputPort.getAccountByCustomerRefId(customerId);
    }
}