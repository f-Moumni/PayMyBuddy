package com.pmb.PayMyBuddy.exceptions;

public class InsufficientFundsException extends Exception {

    public InsufficientFundsException() {
    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}
