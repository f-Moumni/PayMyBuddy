package com.pmb.PayMyBuddy.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "bank_account")
public class BankAccount {
    /**
     * Bank account ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbank_account")
    private long id;

    /**
     * Bank account Iban
     */
    @NotBlank
    private String iban;
    @NotBlank

    /**
     * Bank account Swift
     */
    private String swift;
    /**
     * bank account Owner
     */
    @OneToOne (cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinColumn(name = "owner")
    private AppUser owner;

    public BankAccount(String iban, String swift, AppUser owner) {
        this.iban = iban;
        this.swift = swift;
        this.owner = owner;
    }


    @OneToMany(mappedBy = "bankAccount", cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    private List<Transfer> transactions = new ArrayList<>();


    public BankAccount() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    public List<Transfer> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transfer> transactions) {
        this.transactions = transactions;
    }
}
