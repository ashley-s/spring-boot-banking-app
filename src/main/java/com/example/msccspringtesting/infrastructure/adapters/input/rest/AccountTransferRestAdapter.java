package com.example.msccspringtesting.infrastructure.adapters.input.rest;


import com.example.msccspringtesting.application.ports.input.AccountTransferUseCase;
import com.example.msccspringtesting.domain.model.AccountTransfer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AccountTransferRestAdapter {

    private final AccountTransferUseCase accountTransferUseCase;

    @PostMapping(value = "/transfers")
    public ResponseEntity<AccountTransfer> createAccountTransfer(@RequestBody @Valid AccountTransfer accountTransfer){
        return new ResponseEntity<>(this.accountTransferUseCase.createAccountTransfer(accountTransfer), HttpStatus.CREATED);
    }

}
