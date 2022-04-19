package com.pmb.PayMyBuddy.DTO;

import com.pmb.PayMyBuddy.constants.Roles;
import com.pmb.PayMyBuddy.model.AppUser;

import java.util.List;

public class JwtResponse {
    private String accessToken;
    private String email;
    public JwtResponse(String token, String email) {
        this.accessToken = token;
    }
    public String getAccessToken() {
        return accessToken;
    }

    public JwtResponse() {

    }
}
