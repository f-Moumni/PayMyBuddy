package com.pmb.PayMyBuddy.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    @Email
    private String mail;

    @NotBlank
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
