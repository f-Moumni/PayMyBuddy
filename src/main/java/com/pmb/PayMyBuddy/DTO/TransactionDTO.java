package com.pmb.PayMyBuddy.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor

public class TransactionDTO {

    private String name;
    private LocalDateTime dateTime;
    private String description;
    private String amount;


    public TransactionDTO(String name, LocalDateTime dateTime, String description, String amount) {
        this.name = name;
        this.dateTime = dateTime;
        this.description = description;
        this.amount = amount;
    }
}
