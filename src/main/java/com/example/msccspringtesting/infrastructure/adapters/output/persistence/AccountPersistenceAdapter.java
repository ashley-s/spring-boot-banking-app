package com.example.msccspringtesting.infrastructure.adapters.output.persistence;

import com.example.msccspringtesting.application.ports.output.AccountOutputPort;
import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.mapper.AccountMapper;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountPersistenceAdapter implements AccountOutputPort {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Cacheable(value = "users", key = "#refId")
    public List<Account> getAccountByCustomerRefId(String refId) {
        log.info("Calling DB");
        return this.accountRepository.findAllByCustomerId(Long.parseLong(refId)).stream().map(this.accountMapper::entityToModel).collect(Collectors.toList());
    }

    @Override
    public Optional<Account> findAccountByAccountId(String accountId) {
        return this.accountRepository.findByAccountNumber(accountId)
                .map(this.accountMapper::entityToModel);
    }

    @Override
    @CacheEvict(value = "users", key = "#account.customer.id + ''")
    public Account updateAccount(Account account) {
        return this.accountMapper.entityToModel(this.accountRepository.save(this.accountMapper.modelToEntity(account)));
    }
}
