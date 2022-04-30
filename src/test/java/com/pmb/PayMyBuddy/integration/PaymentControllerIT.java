package com.pmb.PayMyBuddy.integration;

import com.pmb.PayMyBuddy.DTO.PaymentDTO;
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
import java.util.Collections;
import java.util.List;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql( scripts = "classpath:test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql( scripts = "classpath:delete-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PaymentControllerIT {
    @Autowired
    private MockMvc mvc;

    private static User user ;
    private static SecurityContext securitycontext ;


    @BeforeAll
    public static void init() {
        securitycontext = new SecurityContextImpl();
        user = new User("john@exemple.fr","password", List.of(new SimpleGrantedAuthority("USER")));
    }
    @BeforeEach
    void setup() throws SQLException {
        securitycontext.setAuthentication(new TestingAuthenticationToken(user, null, Collections.emptyList()));
        SecurityContextHolder.setContext(securitycontext);
    }

    @Test
    @Tag("doPayment")
    void testDoPayment_shouldReturnTrue() throws Exception {
        //ARRANGE
        PaymentDTO paymentDTO = new PaymentDTO("doe@exemple.fr", 20, "movie");
        //ACT //ASSERT
        mvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonTestMapper.asJsonString(paymentDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("payment done"))
                .andExpect(jsonPath("$['statusCode']").value(201))
                .andExpect(jsonPath("$.data['payment']").value(true));

    }

    @Test
    @Tag("doPayment")
    void testDoPayment_shouldReturnStatusConflict() throws Exception {
        //ARRANGE
        PaymentDTO paymentDTO = new PaymentDTO("doe@exemple.fr", 200, "movie");
        //ACT //ASSERT
        mvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonTestMapper.asJsonString(paymentDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("poor balance")));

    }

}
