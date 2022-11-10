package com.example.msccspringtesting.application.ports.input;


import com.example.msccspringtesting.domain.model.Transaction;

import java.util.List;

public interface TransactionUseCase {
    List<Transaction> getTransactionsByAccountId(String accountId);
}
