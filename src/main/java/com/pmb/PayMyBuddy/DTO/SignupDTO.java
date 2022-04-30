package com.pmb.PayMyBuddy.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class SignupDTO {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @DateTimeFormat
    private LocalDate birthDate;
    @Email(message = "invalid email")
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


    public String getLastName() {
        return lastName;
    }


    public LocalDate getBirthDate() {
        return birthDate;
    }


    public String getMail() {
        return mail;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
