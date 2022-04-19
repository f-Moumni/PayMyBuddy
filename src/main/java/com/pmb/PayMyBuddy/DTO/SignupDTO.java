package com.pmb.PayMyBuddy.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pmb.PayMyBuddy.util.DateHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {
    private String firstName;
    private String lastName;
    @JsonDeserialize(using = DateHandler.class)
    private LocalDate birthDate;
    @Email(message = "invalid email ")
    private String mail;
    private String password;

}
