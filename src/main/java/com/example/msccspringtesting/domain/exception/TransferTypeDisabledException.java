package com.example.msccspringtesting.domain.exception;

public class TransferTypeDisabledException extends RuntimeException{
    public TransferTypeDisabledException(String message) {
        super(message);
    }
}
