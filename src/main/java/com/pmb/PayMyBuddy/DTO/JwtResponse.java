package com.pmb.PayMyBuddy.DTO;

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
