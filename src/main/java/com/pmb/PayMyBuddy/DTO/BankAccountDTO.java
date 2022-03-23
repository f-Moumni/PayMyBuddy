package com.pmb.paymybuddy.DTO;

import lombok.Data;

@Data
public class BankAccountDTO {
    private String Iban;
    private String swift;

    public BankAccountDTO(String iban, String swift) {
        Iban = iban;
        this.swift = swift;
    }
}
