package com.pmb.PayMyBuddy.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.jta.atomikos.AtomikosDependsOnBeanFactoryPostProcessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrincipalUserTest {
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContextHolder securityContextHolder;
    @Mock
    private SecurityContext securityContext;

    private PrincipalUser principalUser;

//todo  test getCurrentUserMail
   // @Test
    @Tag("getCurrentUserMail")
    @DisplayName("test getCurrentUserMail for Positive Scenario")
    public void testGetCurrentUserMail_PositiveScenario() {
        //ARRANGE
        User user = new User("john@email.fr", "password", new ArrayList<>());
       doReturn(securityContext) .when(securityContextHolder).getContext();
       doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(user).when(authentication.getPrincipal());
        //ACT
        String result = principalUser.getCurrentUserMail();
        //ASSERT
        assertThat(result).isEqualTo("john@email.fr");

    }


}
