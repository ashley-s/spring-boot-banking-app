package com.example.msccspringtesting.application.ports.input;


import com.example.msccspringtesting.domain.model.Transaction;

import java.util.List;

/**
 * Input port to get list of transactions for an account
 */
public interface TransactionUseCase {
    List<Transaction> getTransactionsByAccountNumber(String accountId);
}
