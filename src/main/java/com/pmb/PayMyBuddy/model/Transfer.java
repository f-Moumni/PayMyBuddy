package com.pmb.PayMyBuddy.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@DiscriminatorValue("TRANSFER")
public class Transfer extends Transaction {

    @ManyToOne
    @JoinColumn(name = "bankaccount")
    private BankAccount bankAccount;

    public Transfer() {

    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Transfer(double amount, double fee, String description, LocalDateTime dateTime, Account debitAccount, Account creditAccount, BankAccount bankAccount) {
        this.setAmount(amount);
        this.setFee(fee);
        this.setDescription(description);
        this.setDateTime(dateTime);
        this.setDebitAccount(debitAccount);
        this.setBankAccount(bankAccount);
        this.setCreditAccount(creditAccount);
    }

}
