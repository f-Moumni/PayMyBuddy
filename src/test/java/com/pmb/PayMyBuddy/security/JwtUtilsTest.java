package com.pmb.PayMyBuddy.security;

import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.AppUser;
import com.pmb.PayMyBuddy.model.Role;
import com.pmb.PayMyBuddy.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {
    private static final String token = "260bce87-6be9-4897-add7-b3b675952538";
    @Mock
    private static Authentication authentication;
    @InjectMocks
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() throws Exception {
        User user = new User("john@email.fr", "password", Collections.emptyList());
        SecurityContext securitycontext = new SecurityContextImpl();
        securitycontext.setAuthentication(new TestingAuthenticationToken(user, null, Collections.emptyList()));
        SecurityContextHolder.setContext(securitycontext);

    }

    //todo fix this test after adding test profile
    // @Test
    @Tag("generateJwtToken")
    @DisplayName("generateJwtToken should return UserDetails of account for a given email")
    void generateJwtToken_Test_shouldGenerateNewToken() {

        //ACT
        String result = jwtUtils.generateJwtToken(authentication);
        //ASSERT
        verify(authentication).getPrincipal();
        assertThat(result).isNotEmpty();
    }


}
