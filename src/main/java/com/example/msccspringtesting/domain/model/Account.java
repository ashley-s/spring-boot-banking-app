package com.example.msccspringtesting.domain.model;

import lombok.Data;


@Data
public class Account {
    private long id;
    private String accountNumber;
    private double currentBalance;
    private Customer customer;

    public void updateBalance(double amount) {
        this.currentBalance += amount;
    }
}
