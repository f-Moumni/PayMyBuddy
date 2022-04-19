package com.pmb.PayMyBuddy.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pmb.PayMyBuddy.util.DateHandler;
import lombok.*;

import javax.validation.constraints.Email;
import java.time.LocalDate;


public class ProfileDTO {

    private String firstName;
    private String lastName;
    @JsonDeserialize(using = DateHandler.class)
    private LocalDate birthdate;
    @Email
    private String mail;

    private double balance;

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

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
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
