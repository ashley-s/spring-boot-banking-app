package com.example.msccspringtesting.application.ports.input;

import com.example.msccspringtesting.domain.model.Account;

import java.util.List;

/**
 * Input port to get accounts for a customer
 */
public interface AccountUseCase {
    List<Account> getAccounts(String customerId);
}
