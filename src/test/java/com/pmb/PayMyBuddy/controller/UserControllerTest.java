package com.pmb.PayMyBuddy.controller;

import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.security.jwt.JwtUtils;
import com.pmb.PayMyBuddy.service.AccountDetailsService;
import com.pmb.PayMyBuddy.service.ITransferService;
import com.pmb.PayMyBuddy.service.IUserService;
import com.pmb.PayMyBuddy.util.JsonTestMapper;
import com.pmb.PayMyBuddy.util.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({TestSecurityConfig.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private IUserService userService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private AccountDetailsService accountDetailsService;
    @MockBean
    private JwtUtils jwtUtils;
    @Autowired
    private WebApplicationContext context;
    ProfileDTO profileDTO;
    SignupDTO signupDTO;

    @BeforeEach
    void setup() {
        profileDTO = new ProfileDTO("john", "doe", LocalDate.now().minusYears(25), "john@exemple.fr", 0);
        signupDTO = new SignupDTO("john", "doe", LocalDate.now().minusYears(25), "john@exemple.fr", "password");
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Tag("SaveUserAccount")
    void testSaveUserAccount_shouldReturnProfileDTO() throws Exception {
        //ARRANGE
        when(userService.addUser(any())).thenReturn(profileDTO);
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
        //ARRANGE
        when(userService.updateUser(any())).thenReturn(profileDTO);
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
        //ARRANGE
        when(userService.getUser()).thenReturn(profileDTO);
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

    @Test
    @Tag("deleteUser")
    void testDeleteUser_shouldReturnTrue() throws Exception {
        //ARRANGE
        when(userService.deleteUser()).thenReturn(true);
        //ACT //ASSERT
        mvc.perform(delete("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Account deleted"))
                .andExpect(jsonPath("$['statusCode']").value(200))
                .andExpect(jsonPath("$.data['account']").value(true));

    }
}
