package com.example.msccspringtesting.domain.exception;

public class TransferFundsNotPossibleException extends RuntimeException{
    public TransferFundsNotPossibleException(String message) {
        super(message);
    }
}
