package com.pmb.PayMyBuddy.controller;

import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.DTO.LoginRequest;
import com.pmb.PayMyBuddy.DTO.Response;
import com.pmb.PayMyBuddy.model.BankAccount;
import com.pmb.PayMyBuddy.security.jwt.AuthEntryPointJwt;
import com.pmb.PayMyBuddy.security.jwt.JwtUtils;
import com.pmb.PayMyBuddy.service.AccountDetailsService;
import com.pmb.PayMyBuddy.service.BankAccountService;
import com.pmb.PayMyBuddy.service.IBankAccountService;
import com.pmb.PayMyBuddy.util.JsonTestMapper;
import com.pmb.PayMyBuddy.util.TestSecurityConfig;
import io.jsonwebtoken.lang.Collections;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({JsonTestMapper.class, TestSecurityConfig.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(BankAccountController.class)
public class BankAccountControllerTest<ApplicationUserService> {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private IBankAccountService bankAccountService;
    @MockBean
    private AuthenticationManager authenticationManager;
   @MockBean
    private AccountDetailsService accountDetailsService;
    @MockBean
   private static JwtUtils jwtUtils;

    @MockBean
    private static Authentication authentication;
    @Autowired
    private WebApplicationContext context;
    private BankAccountDTO bankAccountDTO;
    private Response response;
    private BankAccount bankAccount;

    @BeforeEach
    void setup() {
        bankAccountDTO = new BankAccountDTO("iban545", "swift1111");
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        response.builder().data(Map.of("bankAccount",bankAccountDTO));

    }

    @Test
    @Tag("bankAccount")
    void testGetBankAccount_shouldReturnBankAccountDTO() throws Exception {

        when(bankAccountService.getBankAccount()).thenReturn(bankAccountDTO);
        //ACT
        mvc.perform(get("/bankAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Bank Account retrieved"))
                .andExpect( jsonPath("$.data['bankAccount'].swift").value(bankAccountDTO.getSwift()))
                .andExpect( jsonPath("$.data['bankAccount'].iban").value(bankAccountDTO.getIban()));
    }
    @Test
    @Tag("saveBankAccount")
    void testSaveBankAccount_shouldReturnTrue() throws Exception {
        when(bankAccountService.addBankAccount(any())).thenReturn(true);
        //ACT
        mvc.perform(post("/bankAccount").content(JsonTestMapper.asJsonString(bankAccountDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Bank Account saved"))
                .andExpect( jsonPath("$.data['bankAccount']").value("true"));

    }

    @Test
    @Tag("updateBankAccount")
    void testUpdateBankAccount_shouldReturnTrue() throws Exception {
        when(bankAccountService.updateBankAccount(any())).thenReturn(true);
        //ACT
        mvc.perform(put("/bankAccount").content(JsonTestMapper.asJsonString(bankAccountDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Bank Account updated"))
                .andExpect( jsonPath("$.data['bankAccount']").value("true"));

    }
}
