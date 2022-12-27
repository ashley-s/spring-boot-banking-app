package com.example.msccspringtesting.application.ports.input;

import com.example.msccspringtesting.domain.model.AccountTransfer;

/**
 * Input port to create a transfer between accounts.
 */
public interface AccountTransferUseCase {
    AccountTransfer createAccountTransfer(AccountTransfer accountTransfer);
}
