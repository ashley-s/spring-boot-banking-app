package com.example.msccspringtesting.infrastructure.adapters.output.persistence.mapper;

import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "accountEntity.accountNumber", target = "accountNumber")
    @Mapping(source = "accountEntity.currentBalance", target = "currentBalance")
    @Mapping(source = "accountEntity.customer", target = "customer")
    Account entityToModel(AccountEntity accountEntity);
    AccountEntity modelToEntity(Account account);
}
