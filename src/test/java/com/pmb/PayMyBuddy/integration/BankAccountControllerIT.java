package com.pmb.PayMyBuddy.integration;

import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.security.jwt.JwtUtils;
import com.pmb.PayMyBuddy.service.AccountDetailsService;
import com.pmb.PayMyBuddy.service.IBankAccountService;
import com.pmb.PayMyBuddy.util.JsonTestMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql( scripts = "classpath:test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql( scripts = "classpath:delete-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BankAccountControllerIT {

    @Autowired
    private MockMvc mvc;

    private BankAccountDTO bankAccountDTO;
    private static User user ;
    private static SecurityContext securitycontext ;
    @BeforeAll
    public static void init() {
        securitycontext = new SecurityContextImpl();
        user = new User("doe@exemple.fr","password", List.of(new SimpleGrantedAuthority("USER")));
    }
    @BeforeEach
    void setup() throws SQLException {
        bankAccountDTO = new BankAccountDTO("iban222222", "swift22222");
        securitycontext.setAuthentication(new TestingAuthenticationToken(user, null, Collections.emptyList()));
        SecurityContextHolder.setContext(securitycontext);
    }
    @Test
    @Tag("bankAccount")
    void testGetBankAccount_shouldReturnBankAccountDTO() throws Exception {
        //ACT //ASSERT
        mvc.perform(get("/bankAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['statusCode']").value(200))
                .andExpect(jsonPath("$['message']").value("Bank Account retrieved"))
                .andExpect(jsonPath("$.data['bankAccount'].swift").value(bankAccountDTO.getSwift()))
                .andExpect(jsonPath("$.data['bankAccount'].iban").value(bankAccountDTO.getIban()));
    }
    @Test
    @Tag("saveBankAccount")
    void testSaveBankAccount_shouldReturnTrue() throws Exception {
        //ACT //ASSERT
        mvc.perform(post("/bankAccount").content(JsonTestMapper.asJsonString(bankAccountDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Bank Account saved"))
                .andExpect(jsonPath("$['statusCode']").value(201))
                .andExpect(jsonPath("$.data['bankAccount']").value("true"));
    }

    @Test
    @Tag("updateBankAccount")
    void testUpdateBankAccount_shouldReturnTrue() throws Exception {
        //ACT //ASSERT
        mvc.perform(put("/bankAccount").content(JsonTestMapper.asJsonString(bankAccountDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['statusCode']").value(200))
                .andExpect(jsonPath("$['message']").value("Bank Account updated"))
                .andExpect(jsonPath("$.data['bankAccount']").value("true"));

    }

}