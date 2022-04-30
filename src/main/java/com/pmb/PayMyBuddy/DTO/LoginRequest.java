package com.pmb.PayMyBuddy.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * the transform object for authentication
 */
public class LoginRequest {

    @Email
    private String mail;

    @Size(min = 6, max = 20)
    private String password;

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

    public LoginRequest(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }
}
