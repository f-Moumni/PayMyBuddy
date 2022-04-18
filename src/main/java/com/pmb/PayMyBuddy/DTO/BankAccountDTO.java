package com.pmb.PayMyBuddy.DTO;


public class BankAccountDTO {
    private String iban;
    private String swift;

    public String getIban() {
        return iban;
    }

    public String getSwift() {
        return swift;
    }

    public BankAccountDTO(String iban, String swift) {
        this.iban = iban;
        this.swift = swift;
    }
}
