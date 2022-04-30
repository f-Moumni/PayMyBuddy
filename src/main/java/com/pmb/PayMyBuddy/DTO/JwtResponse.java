package com.pmb.PayMyBuddy.DTO;

/**
 * transform object to JWT
 */
public class JwtResponse {
    private String accessToken;

    public JwtResponse(String jwt) {
        this.accessToken = jwt;
    }

    public String getAccessToken() {
        return accessToken;
    }

}
