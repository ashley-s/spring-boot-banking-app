package com.example.msccspringtesting.application.ports.output;

import com.example.msccspringtesting.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountOutputPort {
    List<Account> getAccountByCustomerRefId(String refId);
    Optional<Account> findAccountByAccountId(String accountId);
    void updateAccount(Account account);
}
