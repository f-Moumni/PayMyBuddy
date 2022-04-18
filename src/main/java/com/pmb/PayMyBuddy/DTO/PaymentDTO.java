package com.pmb.PayMyBuddy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private String creditAccountEmail;
    private double amount;
    @NotNull
    private String description;

    public PaymentDTO( String creditAccountEmail, double amount, String description) {
        this.creditAccountEmail = creditAccountEmail;
        this.amount = amount;
        this.description = description;
    }
}
