package com.pmb.PayMyBuddy.integration;

import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.security.jwt.JwtUtils;
import com.pmb.PayMyBuddy.service.AccountDetailsService;
import com.pmb.PayMyBuddy.service.IBankAccountService;
import com.pmb.PayMyBuddy.util.JsonTestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
//@Sql(scripts = "../resources/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BankAccountControllerIT {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private IBankAccountService bankAccountService;
   /* @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private AccountDetailsService accountDetailsService;
    @MockBean
    private JwtUtils jwtUtils;*/

    @Autowired
    private WebApplicationContext context;
    private BankAccountDTO bankAccountDTO;


    @BeforeEach
    void setup() {
        bankAccountDTO = new BankAccountDTO("iban545", "swift1111");
      //  mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

   // @Test
    @Tag("saveBankAccount")
    void testSaveBankAccount_shouldReturnTrue() throws Exception {
        //ARRANGE

        //ACT //ASSERT
        mvc.perform(post("/bankAccount").content(JsonTestMapper.asJsonString(bankAccountDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("Bank Account saved"))
                .andExpect(jsonPath("$['statusCode']").value(201))
                .andExpect(jsonPath("$.data['bankAccount']").value("true"));

    }

}
