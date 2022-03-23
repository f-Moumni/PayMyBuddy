package com.pmb.PayMyBuddy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private String debitAccountEmail;
    private String creditAccountEmail;
    private double amount;
    private String description;

    public PaymentDTO(String debitAccountEmail, String creditAccountEmail, double amount, String description) {
        this.debitAccountEmail = debitAccountEmail;
        this.creditAccountEmail = creditAccountEmail;
        this.amount = amount;
        this.description = description;
    }
}
