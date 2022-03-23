package com.pmb.PayMyBuddy.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbank_account")
    private long id;
    @NotBlank
    private String iban;
    @NotBlank
    private String swift;

    @OneToOne (cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinColumn(name = "owner")
    private User owner;

    public BankAccount(String iban, String swift, User owner) {
        this.iban = iban;
        this.swift = swift;
        this.owner = owner;
    }

    @OneToMany(mappedBy = "creditBankAccount", cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    private Set<Transfer> transactions = new TreeSet<>();



}
