package com.example.msccspringtesting.domain.service;

import com.example.msccspringtesting.application.ports.input.TransactionUseCase;
import com.example.msccspringtesting.application.ports.output.TransactionOutputPort;
import com.example.msccspringtesting.domain.model.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TransactionService implements TransactionUseCase {

    private final TransactionOutputPort transactionOutputPort;

    @Override
    public List<Transaction> getTransactionsByAccountId(String accountId) {
        return this.transactionOutputPort.getTransactionByAccountId(accountId);
    }
}
