package com.example.msccspringtesting.application.ports.output;

import com.example.msccspringtesting.domain.event.AccountTransferEvent;

/**
 * Output port to publish event to be
 * consumed
 */
public interface AccountTransferEventPublisher {
    void publishSuccessfulTransferEvent(AccountTransferEvent accountTransferEvent);
}
