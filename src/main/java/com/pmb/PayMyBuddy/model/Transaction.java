package com.pmb.PayMyBuddy.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter

@Entity(name = "transaction")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="transaction_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Transaction {

    /**
     * transaction id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtransaction")
    private long id;
    /**
     * transaction's amount
     */
    @Column(name = "amount")
    @NotNull(message = "the amount cannot be null")
    private double amount;

    /**
     * fee of transaction
     */
    @Column(name = "fee")
    @NotNull(message = "the fee cannot be null")
    private double fee;
    /**
     * transaction's description
     */
    @Column(name = "description")
    private String description;

    /**
     * date of transaction
     */
    @Column(name = "date")
    @NotNull(message = "the date cannot be null")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "debit_account")
    private Account debitAccount;
    @ManyToOne
    @JoinColumn(name = "credit_account")
    private Account creditAccount ;

}
