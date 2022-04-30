package com.pmb.PayMyBuddy.integration;

import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.constants.OperationType;
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
public class TransferControllerIT {

    @Autowired
    private MockMvc mvc;

    private static User user ;
    private static SecurityContext securitycontext ;
    @BeforeAll
    public static void init() {
        securitycontext = new SecurityContextImpl();
        user = new User("doe@exemple.fr","password", List.of(new SimpleGrantedAuthority("USER")));
    }
    @BeforeEach
    void setup() throws SQLException {
        securitycontext.setAuthentication(new TestingAuthenticationToken(user, null, Collections.emptyList()));
        SecurityContextHolder.setContext(securitycontext);
    }

    @Test
    @Tag("doTransfer")
    void testDoTransfer_shouldReturnTrue() throws Exception {
        //ARRANGE
        TransferDTO transferDTO = new TransferDTO( 20, "movie", OperationType.DEBIT);

        //ACT //ASSERT
        mvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonTestMapper.asJsonString(transferDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("transfer done"))
                .andExpect(jsonPath("$['statusCode']").value(200))
                .andExpect(jsonPath("$.data['transfer']").value(true));

    }

    @Test
    @Tag("doTransfer")
    void testDoTransfer_shouldReturnStatusConflict() throws Exception {
        //ARRANGE
        TransferDTO transferDTO = new TransferDTO( 200, "movie", OperationType.DEBIT);

        //ACT //ASSERT
        mvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonTestMapper.asJsonString(transferDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("poor balance")));

    }

}
