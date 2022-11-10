package com.example.msccspringtesting.application.ports.output;

import com.example.msccspringtesting.domain.event.AccountTransferEvent;

public interface AccountTransferEventPublisher {
    void publishSuccessfulTransferEvent(AccountTransferEvent accountTransferEvent);
}
