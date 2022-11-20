package com.example.msccspringtesting.infrastructure.adapters.input.rest;

import com.example.msccspringtesting.domain.exception.*;
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

    @ExceptionHandler(value = {AccountNotFoundException.class,
    CustomerNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleExceptionNotFound(RuntimeException ex, WebRequest request) {
        var message = new ErrorMessage(ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AccountTransferFailedException.class})
    public ResponseEntity<ErrorMessage> handleInternalServerError(RuntimeException ex, WebRequest request) {
        var message = new ErrorMessage(ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Data
    @RequiredArgsConstructor
    static class ErrorMessage {
        private final String description;
    }
}
