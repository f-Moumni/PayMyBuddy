package com.pmb.PayMyBuddy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
@ControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFoundException(
            DataNotFoundException e, WebRequest request) {

        ExceptionDetails exception = new ExceptionDetails(LocalDateTime.now(),
                e.getMessage(), HttpStatus.NOT_FOUND,
                request.getDescription(false));
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);

    }

   @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Object> handleInsufficientFundsException(
            InsufficientFundsException e, WebRequest request) {

        ExceptionDetails exception = new ExceptionDetails(LocalDateTime.now(),
                e.getMessage(), HttpStatus.CONFLICT,
                request.getDescription(false));
        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);

    }
  @ExceptionHandler(BalanceNotEmptyException.class)
    public ResponseEntity<Object> handleBalanceNotEmptyException(
            BalanceNotEmptyException e, WebRequest request) {

        ExceptionDetails exception = new ExceptionDetails(LocalDateTime.now(),
                e.getMessage(), HttpStatus.CONFLICT,
                request.getDescription(false));
        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);

    }
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(
            AlreadyExistsException e, WebRequest request) {

        ExceptionDetails exception = new ExceptionDetails(LocalDateTime.now(),
                e.getMessage(), HttpStatus.CONFLICT,
                request.getDescription(false));
        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);

    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException e, WebRequest request) {

        ExceptionDetails exception = new ExceptionDetails(LocalDateTime.now(),
                e.getMessage(), HttpStatus.CONFLICT,
                request.getDescription(false));
        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);

    }
}
