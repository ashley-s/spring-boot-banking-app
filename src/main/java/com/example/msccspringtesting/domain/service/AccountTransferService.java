package com.example.msccspringtesting.domain.service;

import com.example.msccspringtesting.application.ports.input.AccountTransferUseCase;
import com.example.msccspringtesting.application.ports.output.AccountTransferEventPublisher;
import com.example.msccspringtesting.application.ports.output.AccountTransferOutputPort;
import com.example.msccspringtesting.domain.event.AccountTransferEvent;
import com.example.msccspringtesting.domain.exception.AccountTransferFailedException;
import com.example.msccspringtesting.domain.exception.FundsInsufficientException;
import com.example.msccspringtesting.domain.exception.TransferFundsNotPossibleException;
import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.domain.model.AccountTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class AccountTransferService implements AccountTransferUseCase {

    private final AccountService accountService;
    private final AccountTransferOutputPort accountTransferOutputPort;
    private final AccountTransferEventPublisher accountTransferEventPublisher;

    /**
     * Handler that will query account numbers if they exist.
     * Verify if the transfer can be done and then update balance accordingly.
     * if success, an event will be published
     * @param accountTransfer
     * @return
     */
    public AccountTransfer createAccountTransfer(AccountTransfer accountTransfer) {
        var senderAccount = this.getAccountByAccountNumber(accountTransfer.getSenderAccount().getAccountNumber());
        var receiverAccount = this.getAccountByAccountNumber(accountTransfer.getReceiverAccount().getAccountNumber());
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setReceiverAccount(receiverAccount);
        AccountTransfer accountTransferSuccessful;
        try {
            accountTransfer.isTransferFundsPossible();
            senderAccount.updateBalance(-accountTransfer.getAmount());
            receiverAccount.updateBalance(accountTransfer.getAmount());
            accountTransfer.setStatus("SUCCESS");
            accountTransfer.setReference(UUID.randomUUID().toString());
            accountTransferSuccessful = this.accountTransferOutputPort.saveAccountTransfer(accountTransfer);
            this.accountTransferEventPublisher.publishSuccessfulTransferEvent(this.mapToEvent(accountTransferSuccessful));
        }catch (FundsInsufficientException | TransferFundsNotPossibleException e) {
            accountTransfer.setStatus("FAILURE");
            accountTransfer.setReference(UUID.randomUUID().toString());
            this.accountTransferOutputPort.saveAccountTransfer(accountTransfer);
            throw e;
        }catch (Exception e) {
            log.debug("Exception message {}", e.getMessage());
            throw new AccountTransferFailedException("Account Transfer has failed;");
        }
        return accountTransferSuccessful;
    }

    /**
     * get account by account number
     * @param accountNumber
     * @return
     */
    private Account getAccountByAccountNumber(String accountNumber) {
        return this.accountService.getAccountByAccountNumber(accountNumber);
    }

    /**
     * Build event object from account transfer model object.
     * @param accountTransfer
     * @return
     */
    private AccountTransferEvent mapToEvent(AccountTransfer accountTransfer) {
        return AccountTransferEvent.builder().transferType(accountTransfer.getTransferType())
                .senderAccount(accountTransfer.getSenderAccount())
                .receiverAccount(accountTransfer.getReceiverAccount())
                .reference(accountTransfer.getReference())
                .build();
    }
}
