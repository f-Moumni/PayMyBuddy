package com.pmb.PayMyBuddy.DTO;

import com.pmb.PayMyBuddy.constants.OperationType;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * transfer data transfer object for new transfer
 */
public class TransferDTO {
    @NotNull
    private double amount;

    private String description ;
    @NotNull
    private OperationType operationType;

    public TransferDTO(double amount, String description, OperationType operationType) {
        this.amount = amount;
        this.description = description;
        this.operationType = operationType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
