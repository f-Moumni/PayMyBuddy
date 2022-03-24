package com.pmb.PayMyBuddy.DTO;

import com.pmb.PayMyBuddy.constants.OperationType;
import lombok.Data;

@Data
public class TransferDTO {

    private String accountEmail;
    private double amount;
    private String description ;
    private OperationType operationType;

    public TransferDTO(String accountEmail, double amount, String description, OperationType operationType) {
        this.accountEmail = accountEmail;
        this.amount = amount;
        this.description = description;
        this.operationType = operationType;
    }
}
