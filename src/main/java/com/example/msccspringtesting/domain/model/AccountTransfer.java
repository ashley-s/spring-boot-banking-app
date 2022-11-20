package com.example.msccspringtesting.domain.model;

import com.example.msccspringtesting.domain.exception.FundsInsufficientException;
import com.example.msccspringtesting.domain.exception.TransferFundsNotPossibleException;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class AccountTransfer {
    private long id;
    private Account senderAccount;
    private Account receiverAccount;
    @DecimalMin("1.00")
    private double amount;
    @NotBlank
    private String transferType;
    private String status;
    private String reference;
    private LocalDateTime dateCreated;

    /**
     * Handler that will verify the transferType and based on that, if accounts
     * pertain to the same customer. Then will check if funds are available.
     */
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

    /**
     *
     * Handler that will check if accounts pertain to the same customer
     * @return
     */
    private boolean verifyIfAccountsAreForSameCustomer() {
        return this.senderAccount.getCustomer().getRefId().equals(this.receiverAccount.getCustomer().getRefId());
    }

    /**
     * Handler that will check if there ae enough funds for the transaction
     */
    private void isFundsAvailableOnSenderAccount() {
        if (this.senderAccount.getCurrentBalance() < amount) {
            throw new FundsInsufficientException("Funds are insufficient on sender's Account");
        }
    }

}
