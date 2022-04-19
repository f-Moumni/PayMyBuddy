package com.pmb.PayMyBuddy.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class PrincipalUser {
   public static String getCurrentUserMail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           authentication.getPrincipal();
        User userDetails = (User) authentication.getPrincipal();
        return userDetails.getUsername();
    }

}
