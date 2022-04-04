package com.pmb.PayMyBuddy.DTO;

import com.pmb.PayMyBuddy.model.AppUser;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    AppUser owner;
    private List<String> roles;
}
