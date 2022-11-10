package com.example.msccspringtesting.domain.exception;

public class FundsInsufficientException extends RuntimeException{
    public FundsInsufficientException(String message) {
        super(message);
    }
}
