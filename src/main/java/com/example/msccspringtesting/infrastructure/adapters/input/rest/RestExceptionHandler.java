package com.example.msccspringtesting.infrastructure.adapters.input.rest;

import com.example.msccspringtesting.domain.exception.CustomerNotActiveException;
import com.example.msccspringtesting.domain.exception.FundsInsufficientException;
import com.example.msccspringtesting.domain.exception.TransferFundsNotPossibleException;
import com.example.msccspringtesting.domain.exception.TransferTypeDisabledException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {FundsInsufficientException.class,
            TransferFundsNotPossibleException.class,
            CustomerNotActiveException.class,
            TransferTypeDisabledException.class })
    public ResponseEntity<ErrorMessage> handleException(RuntimeException ex, WebRequest request) {
        var message = new ErrorMessage(ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @Data
    @RequiredArgsConstructor
    static class ErrorMessage {
        private final String description;
    }
}
