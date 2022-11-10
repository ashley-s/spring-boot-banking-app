package com.example.msccspringtesting.application.ports.output;

import com.example.msccspringtesting.domain.model.AccountTransfer;

public interface AccountTransferOutputPort {
    AccountTransfer saveAccountTransfer(AccountTransfer accountTransfer);
}
