package com.example.msccspringtesting.infrastructure.adapters.output.persistence;

import com.example.msccspringtesting.application.ports.output.TransactionOutputPort;
import com.example.msccspringtesting.domain.model.Transaction;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.mapper.TransactionMapper;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.repository.TransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements TransactionOutputPort {

    private final TransactionsRepository transactionsRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public List<Transaction> getTransactionByAccountId(String accountId) {
        return this.transactionsRepository.findAllByAccountOwnerAccountNumber(accountId).stream()
                .map(this.transactionMapper::entityToModel).collect(Collectors.toList());
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return this.transactionMapper.entityToModel(this.transactionsRepository.save(this.transactionMapper.modelToEntity(transaction)));
    }
}
