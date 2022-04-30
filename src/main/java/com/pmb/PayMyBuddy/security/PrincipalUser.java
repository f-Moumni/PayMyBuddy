package com.pmb.PayMyBuddy.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * get the current user from
 */
@Component
public class PrincipalUser {

    /**
     * get the current user
     * @return email of the current user
     */
   public String getCurrentUserMail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        return userDetails.getUsername();
    }

}
