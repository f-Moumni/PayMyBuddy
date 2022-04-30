package com.pmb.PayMyBuddy.DTO;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * the transform object profile
 */
public class ProfileDTO {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @DateTimeFormat
    private LocalDate birthdate;
    @Email
    private String mail;

    private double balance;

    public String getFirstName() {
        return firstName;
    }



    public String getLastName() {
        return lastName;
    }


    public LocalDate getBirthdate() {
        return birthdate;
    }



    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ProfileDTO(String firstName, String lastName, LocalDate birthdate, String mail, double balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.mail = mail;
        this.balance = balance;
    }
}
