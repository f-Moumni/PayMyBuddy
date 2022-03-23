package com.pmb.PayMyBuddy.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue("payment")
public class Payment extends Transaction{

    @ManyToOne
    @JoinColumn(name = "credit_account")
    private Account creditAccount ;

    public Payment(double amount, double fee, String description, LocalDateTime dateTime, Account debitAccount, Account creditAccount) {
        this.setAmount(amount);
        this.setFee(fee);
        this.setDescription(description);
        this.setDateTime(dateTime);
        this.setDebitAccount(debitAccount);
        this.setCreditAccount(creditAccount);
    }

}
