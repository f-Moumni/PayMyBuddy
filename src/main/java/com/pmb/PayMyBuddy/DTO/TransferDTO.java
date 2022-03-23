package com.pmb.PayMyBuddy.DTO;

import lombok.Data;
import org.apache.logging.log4j.message.StringFormattedMessage;
@Data
public class TransferDTO {

    private String accountEmail;
    private double amount;
    private String description ;

    public TransferDTO(String accountEmail, double amount, String description) {
        this.accountEmail = accountEmail;
        this.amount = amount;
        this.description = description;
    }
}
