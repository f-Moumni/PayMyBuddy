package com.pmb.PayMyBuddy.DTO;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BankAccountDTO {
    @NotBlank(message = "iban can not be empty or blank")
    @NotNull(message = "iban can not be null")
    private String iban;
    @NotBlank(message = "swift can not be empty or blank")
    @NotNull(message = "swift can not be null")
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
