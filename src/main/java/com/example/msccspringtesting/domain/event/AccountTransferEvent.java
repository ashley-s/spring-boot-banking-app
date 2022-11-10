package com.example.msccspringtesting.domain.event;

import com.example.msccspringtesting.domain.model.Account;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountTransferEvent {
    private Account senderAccount;
    private Account receiverAccount;
    private double amount;
    private String transferType;
    private String reference;
}
