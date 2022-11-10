package com.example.msccspringtesting.domain.exception;

public class CustomerNotActiveException extends RuntimeException{
    public CustomerNotActiveException(String message) {
        super(message);
    }
}
