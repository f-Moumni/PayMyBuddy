package com.pmb.PayMyBuddy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * the transform object for new payment
 */
public class PaymentDTO {
    @Email
    private String creditAccountEmail;
    @NotNull (message = "amount can not be null")
    private double amount;
    @NotNull
    private String description;

    public PaymentDTO( String creditAccountEmail, double amount, String description) {
        this.creditAccountEmail = creditAccountEmail;
        this.amount = amount;
        this.description = description;
    }



    public String getCreditAccountEmail() {
        return creditAccountEmail;
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
}
