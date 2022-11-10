package com.example.msccspringtesting.infrastructure.adapters.output.persistence;

import com.example.msccspringtesting.application.ports.output.AccountOutputPort;
import com.example.msccspringtesting.application.ports.output.AccountTransferOutputPort;
import com.example.msccspringtesting.application.ports.output.TransactionOutputPort;
import com.example.msccspringtesting.domain.model.AccountTransfer;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.mapper.AccountTransferMapper;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.mapper.TransactionMapper;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.repository.AccountTransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AccountTransferPersistenceAdapter implements AccountTransferOutputPort {

    private final AccountOutputPort accountOutputPort;
    private final TransactionOutputPort transactionOutputPort;
    private final AccountTransferRepository accountTransferRepository;
    private final AccountTransferMapper accountTransferMapper;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public AccountTransfer saveAccountTransfer(AccountTransfer accountTransfer) {
        var accountTransferEntity = this.accountTransferRepository.save(this.accountTransferMapper.modelToEntity(accountTransfer));
        if (accountTransfer.getStatus().equals("SUCCESS")) {
            this.accountOutputPort.updateAccount(accountTransfer.getSenderAccount());
            this.accountOutputPort.updateAccount(accountTransfer.getReceiverAccount());
            this.transactionOutputPort.saveTransaction(this.transactionMapper.accountTransferToTransaction(accountTransferEntity));
        }
        return this.accountTransferMapper.entityToModel(accountTransferEntity);
    }
}
