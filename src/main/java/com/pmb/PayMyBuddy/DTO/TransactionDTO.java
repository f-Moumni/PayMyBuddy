package com.pmb.paymybuddy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor

public class TransactionDTO {


    private String name;
    private LocalDateTime dateTime;
    private String description;
    private double fee;
    private String amount;
    public TransactionDTO(String name, LocalDateTime dateTime, String description, double fee, String amount) {
        this.name = name;
        this.dateTime = dateTime;
        this.description = description;
        this.fee = fee;
        this.amount = amount;
    }

}
