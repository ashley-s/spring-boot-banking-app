package com.example.msccspringtesting.infrastructure.adapters.aspectIT;

import com.example.msccspringtesting.domain.event.AccountTransferEvent;
import com.example.msccspringtesting.domain.exception.CustomerNotActiveException;
import com.example.msccspringtesting.domain.model.Account;
import com.example.msccspringtesting.domain.model.AccountTransfer;
import com.example.msccspringtesting.domain.model.Customer;
import com.example.msccspringtesting.domain.service.AccountTransferService;
import com.example.msccspringtesting.infrastructure.adapters.config.PaymentTypesConfig;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@EnableConfigurationProperties(PaymentTypesConfig.class)
@TestPropertySource(value = "classpath:application.properties")
@Import(value = CreateAccountTransferIT.EventConsumer.class)
class CreateAccountTransferIT {

    @Autowired
    private AccountTransferService accountTransferService;
    @SpyBean
    private EventConsumer eventConsumer;

    @TestComponent
    @Slf4j
    public static class EventConsumer {
        @EventListener
        public void getEvents(AccountTransferEvent accountTransferEvent) {
            log.info("Event consumed {}", accountTransferEvent);
        }
    }

    @Test
    @WithCustomMockUser(username = "1022")
    @Sql(scripts = "classpath:init_scripts.sql")
    @Order(1)
    void should_publish_event_for_successful_transfer() {
        this.accountTransferService.createAccountTransfer(buildAccountTransferSuccessful());
        Mockito.verify(this.eventConsumer, Mockito.times(1)).getEvents(ArgumentMatchers.any(AccountTransferEvent.class));
    }

    @Test
    @WithCustomMockUser(username = "1025")
    @Order(2)
    void should_not_publish_event_for_disabled_customer() {
        Assertions.assertThatThrownBy(() -> this.accountTransferService.createAccountTransfer(buildAccountTransferSuccessful())).isInstanceOf(CustomerNotActiveException.class);
        Mockito.verify(eventConsumer, Mockito.times(0)).getEvents(ArgumentMatchers.any(AccountTransferEvent.class));
    }

    private static AccountTransfer buildAccountTransferSuccessful() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setRefId("1022");
        customer.setActive(true);
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("00123456789");
        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("00777456789");
        AccountTransfer accountTransfer = new AccountTransfer();
        accountTransfer.setReceiverAccount(receiverAccount);
        accountTransfer.setSenderAccount(senderAccount);
        accountTransfer.setAmount(1.00);
        accountTransfer.setTransferType("OWN");
        return accountTransfer;
    }


}