package com.example.msccspringtesting.infrastructure.adapters.output.eventpublisher;

import com.example.msccspringtesting.application.ports.output.AccountTransferEventPublisher;
import com.example.msccspringtesting.domain.event.AccountTransferEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class AccountTransferEventPublisherAdapter implements AccountTransferEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Async
    public void publishSuccessfulTransferEvent(AccountTransferEvent accountTransferEvent) {
        log.info("Event published {}", accountTransferEvent);
        this.applicationEventPublisher.publishEvent(accountTransferEvent);
    }
}
