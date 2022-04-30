package com.pmb.PayMyBuddy.DTO;

import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.constants.TransactionType;

import java.time.LocalDateTime;

/**
 * transaction transform object
 */
public class TransactionDTO {

    private String name;
    private LocalDateTime dateTime;
    private String description;
    private double amount;
    private OperationType operationType;
    private TransactionType transactionType;
    public String getName() {
        return name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }



    public TransactionDTO(String name, LocalDateTime dateTime, String description, double amount, OperationType operationType, TransactionType transactionType) {
        this.name = name;
        this.dateTime = dateTime;
        this.description = description;
        this.amount = amount;
        this.operationType = operationType;
        this.transactionType = transactionType;
    }
}
