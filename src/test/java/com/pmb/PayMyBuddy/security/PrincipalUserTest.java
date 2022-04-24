package com.pmb.PayMyBuddy.security;

;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;


import org.springframework.security.authentication.TestingAuthenticationToken;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;



import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrincipalUserTest {


    private PrincipalUser principalUser = new PrincipalUser();


    @Test
    @Tag("getCurrentUserMail")
    @DisplayName("test getCurrentUserMail for Positive Scenario")
    public void testGetCurrentUserMail_PositiveScenario() {
        //ARRANGE
        User user = new User("john@email.fr", "password",Collections.emptyList());
        SecurityContext securitycontext = new SecurityContextImpl();
        securitycontext.setAuthentication(new TestingAuthenticationToken(user, null, Collections.emptyList()));
        SecurityContextHolder.setContext(securitycontext);
        //ACT
        String result = principalUser.getCurrentUserMail();
        //ASSERT
        assertThat(result).isEqualTo("john@email.fr");

    }


}
