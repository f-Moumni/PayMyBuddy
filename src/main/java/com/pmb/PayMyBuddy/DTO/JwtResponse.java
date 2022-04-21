package com.pmb.PayMyBuddy.DTO;

import com.pmb.PayMyBuddy.constants.Roles;
import com.pmb.PayMyBuddy.model.AppUser;

import java.util.List;

public class JwtResponse {
    private String accessToken;



    public JwtResponse(String jwt) {
        this.accessToken =jwt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public JwtResponse() {

    }
}
