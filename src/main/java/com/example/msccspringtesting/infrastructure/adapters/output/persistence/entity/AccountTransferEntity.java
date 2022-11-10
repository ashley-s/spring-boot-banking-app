package com.example.msccspringtesting.infrastructure.adapters.output.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "account_transfers")
public class AccountTransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "account_sender_id")
    private AccountEntity senderAccountEntity;
    @OneToOne
    @JoinColumn(name = "account_receiver_id")
    private AccountEntity receiverAccountEntity;
    private double amount;
    private String transferType;
    private String status;
    private String reference;
    private LocalDateTime dateCreated;

    @PrePersist
    public void createdAt() {
        this.dateCreated = LocalDateTime.now();
    }
}
