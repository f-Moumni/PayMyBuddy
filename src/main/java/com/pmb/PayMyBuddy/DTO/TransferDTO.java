package com.pmb.PayMyBuddy.DTO;

import com.pmb.PayMyBuddy.constants.OperationType;
import lombok.Data;

@Data
public class TransferDTO {

    private double amount;
    private String description ;
    private OperationType operationType;

    public TransferDTO(double amount, String description, OperationType operationType) {
        this.amount = amount;
        this.description = description;
        this.operationType = operationType;
    }
}
