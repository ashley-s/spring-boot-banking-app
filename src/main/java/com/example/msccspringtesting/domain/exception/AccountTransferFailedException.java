package com.example.msccspringtesting.domain.exception;

public class AccountTransferFailedException extends RuntimeException{
    public AccountTransferFailedException(String message) {
        super(message);
    }
}
