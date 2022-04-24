package com.pmb.PayMyBuddy.controller;

import com.pmb.PayMyBuddy.DTO.LoginRequest;
import com.pmb.PayMyBuddy.security.PasswordEncoder;
import com.pmb.PayMyBuddy.security.jwt.AuthEntryPointJwt;
import com.pmb.PayMyBuddy.security.jwt.JwtUtils;
import com.pmb.PayMyBuddy.service.AccountDetailsService;
import com.pmb.PayMyBuddy.util.JsonTestMapper;
import com.pmb.PayMyBuddy.util.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import static org.mockito.Mockito.when;


@Import({JsonTestMapper.class,TestSecurityConfig.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private static Authentication authentication;
    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    private static JwtUtils jwtUtils;

    @MockBean
    private AccountDetailsService accountDetailsService;
    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void setup() {
           mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Tag("authenticateUser")
    void authenticateUser_returnJWT() throws Exception {
        // ARRANGE
        LoginRequest loginRequest = new LoginRequest("john@email.fr", "password");
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("token");
        //ACT
        mvc.perform(MockMvcRequestBuilders.post("/sign-in").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JsonTestMapper.asJsonString(loginRequest)))
                .andExpect(status().isOk());
    }


}
