package com.example.msccspringtesting.infrastructure.adapters.input.rest;


import com.example.msccspringtesting.application.ports.input.AccountUseCase;
import com.example.msccspringtesting.domain.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AccountRestAdapter {

    private final AccountUseCase accountUseCase;

    @GetMapping(value = "/customers/{customerRefId}/accounts")
    public ResponseEntity<List<Account>> getAccounts(@PathVariable String customerRefId){
        return new ResponseEntity<>(this.accountUseCase.getAccounts(customerRefId), HttpStatus.OK);
    }

}
