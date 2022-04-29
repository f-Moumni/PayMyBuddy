package com.pmb.PayMyBuddy.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;



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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Account getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(Account debitAccount) {
        this.debitAccount = debitAccount;
    }

    public Account getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(Account creditAccount) {
        this.creditAccount = creditAccount;
    }
}
