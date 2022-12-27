package com.example.msccspringtesting.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Model a transaction that happens after a
 * successful account transfer between two accounts.
 */
@Data
public class Transaction {
    private long id;
    private Account senderAccount;
    private Account receiverAccount;
    private double amount;
    private LocalDateTime completionDate;
    private String reference;
    private Account accountOwner;
}
