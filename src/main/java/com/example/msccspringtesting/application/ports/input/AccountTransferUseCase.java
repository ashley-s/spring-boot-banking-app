package com.example.msccspringtesting.application.ports.input;

import com.example.msccspringtesting.domain.model.AccountTransfer;

public interface AccountTransferUseCase {
    AccountTransfer createAccountTransfer(AccountTransfer accountTransfer);
}
