package com.example.msccspringtesting.application.ports.output;

import com.example.msccspringtesting.domain.model.Transaction;

import java.util.List;

public interface TransactionOutputPort {
    List<Transaction> getTransactionByAccountId(String accountId);
    Transaction saveTransaction(Transaction transaction);
}
