package com.example.msccspringtesting.application.ports.output;

import com.example.msccspringtesting.domain.model.AccountTransfer;

/**
 * Output port to save an account transfer
 */
public interface AccountTransferOutputPort {
    AccountTransfer saveAccountTransfer(AccountTransfer accountTransfer);
}
