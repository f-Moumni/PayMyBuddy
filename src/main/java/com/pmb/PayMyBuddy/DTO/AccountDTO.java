package com.pmb.paymybuddy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    @Email
    private String mail;
    private String password;
    private double balance;


}
