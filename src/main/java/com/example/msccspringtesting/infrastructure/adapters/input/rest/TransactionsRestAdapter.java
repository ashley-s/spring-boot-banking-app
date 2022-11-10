package com.example.msccspringtesting.infrastructure.adapters.input.rest;


import com.example.msccspringtesting.application.ports.input.TransactionUseCase;
import com.example.msccspringtesting.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TransactionsRestAdapter {

    private final TransactionUseCase transactionUseCase;

    @GetMapping(value = "accounts/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String accountNumber){
        return new ResponseEntity<>(this.transactionUseCase.getTransactionsByAccountId(accountNumber), HttpStatus.OK);
    }

}
