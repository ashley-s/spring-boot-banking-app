package com.example.msccspringtesting.application.ports.input;

import com.example.msccspringtesting.domain.model.Account;

import java.util.List;

public interface AccountUseCase {
    List<Account> getAccounts(String customerId);
}
