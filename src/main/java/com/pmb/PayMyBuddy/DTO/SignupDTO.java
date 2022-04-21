package com.pmb.PayMyBuddy.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pmb.PayMyBuddy.util.DateHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.time.LocalDate;

public class SignupDTO {
    private String firstName;
    private String lastName;
    @JsonDeserialize(using = DateHandler.class)
    private LocalDate birthDate;
    @Email(message = "invalid email ")
    private String mail;
    private String password;

    public SignupDTO(String firstName, String lastName, LocalDate birthDate, String mail, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.mail = mail;
        this.password = password;
    }

    public SignupDTO() {
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
}
