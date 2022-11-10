package com.example.msccspringtesting.domain.service;

import com.example.msccspringtesting.application.ports.input.AccountTransferUseCase;
import com.example.msccspringtesting.application.ports.output.AccountTransferEventPublisher;
import com.example.msccspringtesting.application.ports.output.AccountTransferOutputPort;
import com.example.msccspringtesting.domain.event.AccountTransferEvent;
import com.example.msccspringtesting.domain.exception.FundsInsufficientException;
import com.example.msccspringtesting.domain.exception.TransferFundsNotPossibleException;
import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.domain.model.AccountTransfer;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AccountTransferService implements AccountTransferUseCase {

    private final AccountService accountService;
    private final AccountTransferOutputPort accountTransferOutputPort;
    private final AccountTransferEventPublisher accountTransferEventPublisher;

    public AccountTransfer createAccountTransfer(AccountTransfer accountTransfer) {
        var senderAccount = this.getAccountByAccountNumber(accountTransfer.getSenderAccount().getAccountNumber());
        var receiverAccount = this.getAccountByAccountNumber(accountTransfer.getReceiverAccount().getAccountNumber());
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setReceiverAccount(receiverAccount);
        try {
            accountTransfer.isTransferFundsPossible();
            senderAccount.updateBalance(-accountTransfer.getAmount());
            receiverAccount.updateBalance(accountTransfer.getAmount());
            accountTransfer.setStatus("SUCCESS");
            accountTransfer.setReference(UUID.randomUUID().toString());
            this.accountTransferEventPublisher.publishSuccessfulTransferEvent(this.mapToEvent(accountTransfer));
        }catch (FundsInsufficientException | TransferFundsNotPossibleException e) {
            accountTransfer.setStatus("FAILURE");
            accountTransfer.setReference(UUID.randomUUID().toString());
            this.accountTransferOutputPort.saveAccountTransfer(accountTransfer);
            throw e;
        }
        return this.accountTransferOutputPort.saveAccountTransfer(accountTransfer);
    }

    private Account getAccountByAccountNumber(String accountNumber) {
        return this.accountService.getAccountByAccountId(accountNumber);
    }

    private AccountTransferEvent mapToEvent(AccountTransfer accountTransfer) {
        return AccountTransferEvent.builder().transferType(accountTransfer.getTransferType())
                .senderAccount(accountTransfer.getSenderAccount())
                .receiverAccount(accountTransfer.getReceiverAccount())
                .reference(accountTransfer.getReference())
                .build();
    }
}
