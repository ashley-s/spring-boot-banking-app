package com.example.msccspringtesting.domain.model;

import com.example.msccspringtesting.domain.exception.FundsInsufficientException;
import com.example.msccspringtesting.domain.exception.TransferFundsNotPossibleException;
import lombok.Data;

@Data
public class AccountTransfer {
    private long id;
    private Account senderAccount;
    private Account receiverAccount;
    private double amount;
    private String transferType;
    private String status;
    private String reference;
    private String customerNumber;

    public void isTransferFundsPossible() {
        if (this.transferType.equals("OWN") && this.verifyIfAccountsAreForSameCustomer()) {
            this.isFundsAvailableOnSenderAccount();
            return;
        }
        if (this.transferType.equals("OTHER") && !this.verifyIfAccountsAreForSameCustomer()) {
            this.isFundsAvailableOnSenderAccount();
            return;
        }
        throw new TransferFundsNotPossibleException("Transfer of funds is not possible");
    }

    private boolean verifyIfAccountsAreForSameCustomer() {
        return this.senderAccount.getCustomer().getRefId().equals(this.receiverAccount.getCustomer().getRefId());
    }

    private void isFundsAvailableOnSenderAccount() {
        if (this.senderAccount.getCurrentBalance() < amount) {
            throw new FundsInsufficientException("Funds are insufficient on sender's Account");
        }
    }

}
