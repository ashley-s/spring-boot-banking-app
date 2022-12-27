package com.example.msccspringtesting.domain.service;

import com.example.msccspringtesting.application.ports.input.TransactionUseCase;
import com.example.msccspringtesting.application.ports.output.TransactionOutputPort;
import com.example.msccspringtesting.domain.model.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TransactionService implements TransactionUseCase {

    private final TransactionOutputPort transactionOutputPort;

    /**
     * Handler to retrieve a list of transactions based
     * @param accountNumber
     * @return
     */
    @Override
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return this.transactionOutputPort.getTransactionByAccountId(accountNumber);
    }
}
