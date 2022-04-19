package com.pmb.PayMyBuddy.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
    private AppUser owner;

    public BankAccount(String iban, String swift, AppUser owner) {
        this.iban = iban;
        this.swift = swift;
        this.owner = owner;
    }
/*

    @OneToMany(mappedBy = "bankAccount", cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    private Set<Transfer> transactions = new TreeSet<>();
*/



}
