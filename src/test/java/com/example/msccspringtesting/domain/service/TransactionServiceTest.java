package com.example.msccspringtesting.domain.service;

import com.example.msccspringtesting.application.ports.output.TransactionOutputPort;
import com.example.msccspringtesting.domain.model.Transaction;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionOutputPort transactionOutputPort;
    @InjectMocks
    private TransactionService transactionService;

    @Test
    void should_return_transactions_for_valid_account_number() {
        Transaction transaction = new Transaction();
        transaction.setAmount(100.00);
        transaction.setReference("20638fc9-65b2-454d-9298-fa5d41f091a4");
        Mockito.when(this.transactionOutputPort.getTransactionByAccountId(ArgumentMatchers.anyString())).thenReturn(List.of(transaction));
        var transactions = this.transactionService.getTransactionsByAccountNumber("123456789");
        Assertions.assertThat(transactions).isNotNull().hasSize(1);
        Assertions.assertThat(transactions.get(0).getAmount()).isEqualTo(100.00);
    }
}