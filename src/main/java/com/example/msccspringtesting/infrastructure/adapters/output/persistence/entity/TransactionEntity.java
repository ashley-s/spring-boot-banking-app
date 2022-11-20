package com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "source_account_id")
    private AccountEntity senderAccount;
    @OneToOne
    @JoinColumn(name = "target_account_id")
    private AccountEntity receiverAccount;
    private double amount;
    private LocalDateTime completionDate;
    private String reference;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accounts_id")
    private AccountEntity accountOwner;

    @PrePersist
    public void setDateCreated() {
        this.completionDate = LocalDateTime.now();
    }
}
