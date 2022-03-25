package com.pmb.PayMyBuddy.exceptions;

public class BalanceNotEmptyException extends Exception {
    public BalanceNotEmptyException() {
    }

    public BalanceNotEmptyException(String message) {
        super(message);
    }
}
