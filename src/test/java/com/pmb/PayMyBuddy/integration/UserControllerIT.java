package com.pmb.PayMyBuddy.integration;

import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.service.IUserService;
import com.pmb.PayMyBuddy.util.JsonTestMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql( scripts = "classpath:test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql( scripts = "classpath:delete-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerIT {

    @Autowired
    private MockMvc mvc;

    private ProfileDTO profileDTO;
    private SignupDTO signupDTO;
    private static User user ;
    private static SecurityContext securitycontext ;
    @BeforeAll
    public static void init() {
        securitycontext = new SecurityContextImpl();
        user = new User("doe@exemple.fr","password", List.of(new SimpleGrantedAuthority("USER")));
    }
    @BeforeEach
    void setup() throws SQLException {
        profileDTO = new ProfileDTO("thomas", "doe", LocalDate.now().minusYears(25), "doe@exemple.fr", 0);
        signupDTO = new SignupDTO("thomas", "doe", LocalDate.now().minusYears(25), "doe@exemple.fr", "password");
        securitycontext.setAuthentication(new TestingAuthenticationToken(user, null, Collections.emptyList()));
        SecurityContextHolder.setContext(securitycontext);
    }

   @Test
    @Tag("SaveUserAccount")
    void testSaveUserAccount_shouldReturnErrorMessage() throws Exception {
        //ACT //ASSERT
        mvc.perform(post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonTestMapper.asJsonString(signupDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("an account already exists with this email doe@exemple.fr")));

   }

    @Test
    @Tag("SaveUserAccount")
    void testSaveUserAccount_shouldReturnProfileDTO() throws Exception {
        //ARRANGE
        signupDTO = new SignupDTO("lola", "doe", LocalDate.now().minusYears(25), "lola@exemple.fr", "password");
        profileDTO = new ProfileDTO("lola", "doe", LocalDate.now().minusYears(25), "lola@exemple.fr", 0);

        //ACT //ASSERT
        mvc.perform(post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonTestMapper.asJsonString(signupDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Account Saved"))
                .andExpect(jsonPath("$['statusCode']").value(201))
                .andExpect(jsonPath("$.data['account'].firstName").value(profileDTO.getFirstName()))
                .andExpect(jsonPath("$.data['account'].lastName").value(profileDTO.getLastName()))
                .andExpect(jsonPath("$.data['account'].mail").value(profileDTO.getMail()));
    }

    @Test
    @Tag("UpdateUserAccount")
    void testUpdateUserAccount_shouldReturnProfileDTO() throws Exception {

        //ACT //ASSERT
        mvc.perform(put("/api/account")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonTestMapper.asJsonString(signupDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Account updated"))
                .andExpect(jsonPath("$['statusCode']").value(200))
                .andExpect(jsonPath("$.data['account'].firstName").value(profileDTO.getFirstName()))
                .andExpect(jsonPath("$.data['account'].lastName").value(profileDTO.getLastName()))
                .andExpect(jsonPath("$.data['account'].mail").value(profileDTO.getMail()));
    }

    @Test
    @Tag("GetProfile")
    void testGetProfile_shouldReturnProfileDTO() throws Exception {

        //ACT //ASSERT
        mvc.perform(get("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Account retrieved"))
                .andExpect(jsonPath("$['statusCode']").value(200))
                .andExpect(jsonPath("$.data['account'].firstName").value(profileDTO.getFirstName()))
                .andExpect(jsonPath("$.data['account'].lastName").value(profileDTO.getLastName()))
                .andExpect(jsonPath("$.data['account'].mail").value(profileDTO.getMail()));
    }
}
