package com.example.msccspringtesting.infrastructure.adapters.output.persistence.mapper;

import com.example.msccspringtesting.domain.model.Transaction;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity.AccountTransferEntity;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "senderAccount.currentBalance")
    @Mapping(ignore = true, target = "senderAccount.customer")
    @Mapping(ignore = true, target = "receiverAccount.currentBalance")
    @Mapping(ignore = true, target = "accountOwner")
    Transaction entityToModel(TransactionEntity transactionEntity);
    TransactionEntity modelToEntity(Transaction transaction);
    @Mapping(target = "senderAccount", source = "accountTransferEntity.senderAccountEntity")
    @Mapping(target = "accountOwner", source = "accountTransferEntity.senderAccountEntity")
    @Mapping(target = "receiverAccount", source = "accountTransferEntity.receiverAccountEntity")
    @Mapping(ignore = true, target = "id")
    Transaction accountTransferToTransaction(AccountTransferEntity accountTransferEntity);
}
