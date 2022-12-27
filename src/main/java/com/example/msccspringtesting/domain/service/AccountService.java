package com.example.msccspringtesting.domain.service;

import com.example.msccspringtesting.application.ports.input.AccountUseCase;
import com.example.msccspringtesting.application.ports.output.AccountOutputPort;
import com.example.msccspringtesting.domain.exception.AccountNotFoundException;
import com.example.msccspringtesting.domain.exception.CustomerNotFoundException;
import com.example.msccspringtesting.domain.model.Account;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AccountService implements AccountUseCase {

    private final AccountOutputPort accountOutputPort;

    /**
     * Handler that will retrieve the model account given an account number
     * @param accountNumber
     * @return
     */
    public Account getAccountByAccountNumber(String accountNumber) {
        return this.accountOutputPort.findAccountByAccountId(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account does not exist"));
    }

    /**
     * Handler to retrieve list of accounts given the customer id.
     * @param customerId
     * @return
     */
    @Override
    public List<Account> getAccounts(String customerId) {
        var accounts =  this.accountOutputPort.getAccountByCustomerRefId(customerId);
        if (accounts.isEmpty()) {
            throw new CustomerNotFoundException("Customer does not exist");
        }
        return accounts;
    }
}
