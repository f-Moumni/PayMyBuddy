package com.pmb.PayMyBuddy.DTO;

import java.time.LocalDateTime;

public class TransactionDTO {

    private String name;
    private LocalDateTime dateTime;
    private String description;
    private String amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public TransactionDTO() {
    }

    public TransactionDTO(String name, LocalDateTime dateTime, String description, String amount) {
        this.name = name;
        this.dateTime = dateTime;
        this.description = description;
        this.amount = amount;
    }
}
