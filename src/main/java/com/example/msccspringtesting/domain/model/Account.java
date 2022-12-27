package com.example.msccspringtesting.domain.model;

import lombok.Data;

/**
 * Modelling the account for a customer and associating
 * the customer class as well.
 */
@Data
public class Account {
    private long id;
    private String accountNumber;
    private double currentBalance;
    private Customer customer;

    /**
     * Handler used to update the current balance
     * of an account given an amount of money to be deposited (positive)
     * or to be withdrawn (negative)
     * @param amount
     */
    public void updateBalance(double amount) {
        this.currentBalance += amount;
    }
}
