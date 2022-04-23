package com.pmb.PayMyBuddy.model;

import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


@Entity()
@Table(name = "user")
public class AppUser {
    /**
     * use id
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser")
    private long userID;

    /**
     * user's first name
     **/
    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "the first name cannot be empty or null")
    private String firstName;

    /**
     * user's last name
     **/
    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "the last name cannot be empty or null")
    private String lastName;

    /**
     * user's birthdate
     **/
    @Column(name = "birthdate")
    private LocalDate birthDate;

    /**
     * user's pay my buddy account
     **/
    @OneToOne(mappedBy = "accountOwner", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    private Account account;

    /**
     * user's bank accounts
     **/
    @OneToOne(
            mappedBy = "owner", cascade = CascadeType.ALL
    )
    private BankAccount bankAccount;

    /**
     * pay may buddy account's contacts
     **/
    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
            }
    )
    @JoinTable(
            name = "contact",
            joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )

    private List<AppUser> contacts = new ArrayList<>();

    public AppUser(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public AppUser() {

    }

    public AppUser(long userID, String firstName, String lastName, LocalDate birthDate, Account account) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.account = account;

    }



    public long getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public List<AppUser> getContacts() {
        return contacts;
    }

    public void setContacts(List<AppUser> contacts) {
        this.contacts = contacts;
    }

    public void removeAccount(AppUser appUser) {
        appUser.account.setEnabled(false);
        appUser.account.setPassword(null);
        appUser.account.setRole(null);
    }

   public void addContact(AppUser appUser) {

        appUser.getContacts().add(this);
        contacts.add(appUser);
    }

    public void removeContact(AppUser appUser) {
        appUser.getContacts().remove(this);
        contacts.remove(appUser);
    }
}
