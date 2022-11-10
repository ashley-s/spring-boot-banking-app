package com.example.msccspringtesting.infrastructure.adapters.output.persistence.mapper;

import com.example.msccspringtesting.domain.model.AccountTransfer;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity.AccountTransferEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountTransferMapper {
    @Mapping(source = "accountTransferEntity.receiverAccountEntity", target = "receiverAccount")
    @Mapping(ignore = true, target = "receiverAccount.currentBalance")
    AccountTransfer entityToModel(AccountTransferEntity accountTransferEntity);
    @Mapping(source = "accountTransfer.senderAccount", target = "senderAccountEntity")
    @Mapping(source = "accountTransfer.receiverAccount", target = "receiverAccountEntity")
    AccountTransferEntity modelToEntity(AccountTransfer accountTransfer);
}
