package com.example.msccspringtesting.infrastructure.adapters.config;

import com.example.msccspringtesting.application.ports.output.AccountTransferEventPublisher;
import com.example.msccspringtesting.domain.service.AccountService;
import com.example.msccspringtesting.domain.service.AccountTransferService;
import com.example.msccspringtesting.domain.service.TransactionService;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.AccountPersistenceAdapter;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.AccountTransferPersistenceAdapter;
import com.example.msccspringtesting.infrastructure.adapters.output.persistence.TransactionPersistenceAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public AccountService accountService(AccountPersistenceAdapter accountPersistenceAdapter) {
        return new AccountService(accountPersistenceAdapter);
    }

    @Bean
    public TransactionService transactionService(TransactionPersistenceAdapter transactionPersistenceAdapter) {
        return new TransactionService(transactionPersistenceAdapter);
    }

    @Bean
    public AccountTransferService accountTransferService(AccountService accountService,
                                                         AccountTransferPersistenceAdapter accountTransferPersistenceAdapter,
                                                         @Qualifier("accountTransferEventPublisherAdapter")AccountTransferEventPublisher accountTransferEventPublisher) {
        return new AccountTransferService(accountService, accountTransferPersistenceAdapter, accountTransferEventPublisher);
    }
}
