package com.example.msccspringtesting.infrastructure.adapters.caching;

import com.example.msccspringtesting.application.ports.output.AccountOutputPort;
import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.domain.model.Customer;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.AccountPersistenceAdapter;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity.AccountEntity;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity.CustomerEntity;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.mapper.AccountMapper;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

@SpringJUnitConfig(AccountCachingTest.CachingTestConfig.class)
class AccountCachingTest {

    @Autowired
    @Spy
    private AccountOutputPort accountPersistenceAdapterUnderTest;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CacheManager cacheManager;

    @TestConfiguration
    @EnableCaching
    public static class CachingTestConfig {

        @Bean
        @Primary
        public AccountOutputPort accountPersistenceAdapter() {
            return new AccountPersistenceAdapter(accountRepository(), accountMapper());
        }

        @Bean
        public AccountRepository accountRepository() {
            return Mockito.mock(AccountRepository.class);
        }

        @Bean
        public AccountMapper accountMapper() {
            return Mockito.mock(AccountMapper.class);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("users");
        }
    }

    @BeforeEach
    void setUp() {
        this.cacheManager.getCache("users").clear();
        Mockito.reset(accountRepository);
        Mockito.reset(accountMapper);
    }

    @Test
    void should_store_accounts_inCache_for_customer_On_SecondCall() {
        Mockito.when(this.accountRepository.findAllByCustomerId(ArgumentMatchers.anyLong())).thenReturn(List.of(new AccountEntity()));
        Mockito.when(this.accountMapper.entityToModel(ArgumentMatchers.any(AccountEntity.class))).thenReturn(getSenderAccount());
        Assertions.assertThat(this.cacheManager.getCache("users").get("1012")).isNull();
        List<Account> accounts = this.accountPersistenceAdapterUnderTest.getAccountByCustomerRefId("1012");
        Assertions.assertThat(accounts).hasSize(1);
        Cache.ValueWrapper accountList = this.cacheManager.getCache("users").get("1012");
        List<Account> accountList1 = (List<Account>) accountList.get();
        Assertions.assertThat(accountList1).isNotNull().hasSize(1);
        List<Account> accountsFromCache = this.accountPersistenceAdapterUnderTest.getAccountByCustomerRefId("1012");
        Assertions.assertThat(accountsFromCache).hasSize(1);
        Mockito.verify(this.accountRepository, Mockito.times(1)).findAllByCustomerId(ArgumentMatchers.anyLong());
        Mockito.verify(this.accountMapper, Mockito.times(1)).entityToModel(ArgumentMatchers.any(AccountEntity.class));
    }

    @Test
    void should_store_accounts_inCache_and_clear_on_update() {
        Mockito.when(this.accountRepository.findAllByCustomerId(ArgumentMatchers.anyLong())).thenReturn(List.of(new AccountEntity()));
        Mockito.when(this.accountMapper.entityToModel(ArgumentMatchers.any(AccountEntity.class))).thenReturn(getSenderAccount());
        Mockito.when(this.accountMapper.modelToEntity(ArgumentMatchers.any(Account.class))).thenReturn(getSenderAccountEntity());
        Mockito.when(this.accountRepository.save(ArgumentMatchers.any(AccountEntity.class))).thenReturn(getSenderAccountEntity());
        Assertions.assertThat(this.cacheManager.getCache("users").get("1012")).isNull();
        List<Account> accounts = this.accountPersistenceAdapterUnderTest.getAccountByCustomerRefId("1012");
        Assertions.assertThat(accounts).hasSize(1);
        Cache.ValueWrapper accountList = this.cacheManager.getCache("users").get("1012");
        List<Account> accountList1 = (List<Account>) accountList.get();
        Assertions.assertThat(accountList1).isNotNull().hasSize(1);
        this.accountPersistenceAdapterUnderTest.updateAccount(getSenderAccount());
        Assertions.assertThat(this.cacheManager.getCache("users").get("1012")).isNull();
        List<Account> accountsSecondCall = this.accountPersistenceAdapterUnderTest.getAccountByCustomerRefId("1012");
        Assertions.assertThat(accountsSecondCall).hasSize(1);
        Mockito.verify(this.accountRepository, Mockito.times(2)).findAllByCustomerId(ArgumentMatchers.anyLong());
        Mockito.verify(this.accountRepository, Mockito.times(1)).save(ArgumentMatchers.any(AccountEntity.class));
        Mockito.verify(this.accountMapper, Mockito.times(3)).entityToModel(ArgumentMatchers.any(AccountEntity.class));
    }

    private static Account getSenderAccount() {
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setCurrentBalance(100.00);
        Customer customer = new Customer();
        customer.setId(1012);
        customer.setRefId("1012");
        account.setCustomer(customer);
        return account;
    }

    private static AccountEntity getSenderAccountEntity() {
        AccountEntity account = new AccountEntity();
        account.setAccountNumber("123456789");
        account.setCurrentBalance(100.00);
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1);
        customer.setRefId("1012");
        account.setCustomer(customer);
        return account;
    }

}