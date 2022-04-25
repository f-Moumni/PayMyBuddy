package com.pmb.PayMyBuddy.integration;

import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.service.IUserService;
import com.pmb.PayMyBuddy.util.JsonTestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserControllerIT {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private IUserService userService;

    @Autowired
    private WebApplicationContext context;
    ProfileDTO profileDTO;
    SignupDTO signupDTO;
    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @WithMockUser(username ="joe" )
   // @Test
    @Tag("SaveUserAccount")
    void testSaveUserAccount_shouldReturnProfileDTO() throws Exception {
        //ARRANGE

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

}
