package com.pmb.paymybuddy.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor

@DiscriminatorValue("transfer")
public class Transfer extends Transaction {

    @ManyToOne(fetch = FetchType.LAZY,cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST})
    @JoinColumn(name = "credit_bankaccount")
    private BankAccount creditBankAccount ;

    public Transfer(double amount, double fee, String description, LocalDateTime dateTime, Account debitAccount,BankAccount bankAccount) {
        this.setAmount(amount);
        this.setFee(fee);
        this.setDescription(description);
        this.setDateTime(dateTime);
        this.setDebitAccount(debitAccount);
        this.setCreditBankAccount(bankAccount);
    }
}
