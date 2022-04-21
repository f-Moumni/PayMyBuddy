package com.pmb.PayMyBuddy.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;



@Entity()
@Table(name = "pmb_account")
public class Account   {
    /**
     * pay may buddy account's id
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private long id;

    /**
     * account's email
     **/
    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "the email cannot be empty or null")
    @Email(message = "please entre email address")
    private String mail;

    /**
     * account's password
     */
    @NotBlank(message = "the password cannot be empty or null")
    private String password;

    /**
     * pay may buddy account's balance
     **/
    @Column( nullable = false)
    private double balance;

    /**
     * pay may buddy account's status
     **/
    private boolean enabled ;

    /**
     * pay may buddy locked status
     **/
    @OneToOne
    @JoinColumn(name = "owner")
    private AppUser accountOwner;

    @ManyToOne
    @JoinColumn(name ="role")
     private Role role ;
    /**
     * All payments received in pay may buddy account
     **/
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "creditAccount",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            })
    private Set<Payment> receivedPayments = new TreeSet<>();


    /**
     * All payments sent from pay may buddy account to contacts
     **/
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "debitAccount",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    }
    )
    private Set<Payment> paymentsSent = new TreeSet<>();

    /**
     * All transfer  from /to bankAccount
     **/
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "bankAccount",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    }
    )
    private Set<Transfer> BankTransfer = new TreeSet<>();
    /**
     * Constructor
     * @param mail
     * @param password
     * @param accountOwner
     */
    public Account(String mail, String password, AppUser accountOwner) {
        this.mail = mail;
        this.password = password;
        this.accountOwner = accountOwner;
    }

    public Account(long id, String mail, String password, double balance, boolean enabled, Role role) {
        this.id = id;
        this.mail = mail;
        this.password = password;
        this.balance = balance;
        this.enabled = enabled;

        this.role = role;
    }

    public Account() {
    }



    public long getId() {
        return id;
    }


    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AppUser getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(AppUser accountOwner) {
        this.accountOwner = accountOwner;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Payment> getReceivedPayments() {
        return receivedPayments;
    }

    public void setReceivedPayments(Set<Payment> receivedPayments) {
        this.receivedPayments = receivedPayments;
    }

    public Set<Payment> getPaymentsSent() {
        return paymentsSent;
    }

    public void setPaymentsSent(Set<Payment> paymentsSent) {
        this.paymentsSent = paymentsSent;
    }

    public Set<Transfer> getBankTransfer() {
        return BankTransfer;
    }

    public void setBankTransfer(Set<Transfer> bankTransfer) {
        BankTransfer = bankTransfer;
    }
}
