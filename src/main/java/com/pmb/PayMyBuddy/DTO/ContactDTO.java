package com.pmb.PayMyBuddy.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
@Data
@AllArgsConstructor
public class ContactDTO {
    private String firstName;
    private String lastName;
    @Email
    private String email;
}
