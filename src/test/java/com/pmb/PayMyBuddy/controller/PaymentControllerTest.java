package com.pmb.PayMyBuddy.controller;

import com.pmb.PayMyBuddy.DTO.ContactDTO;
import com.pmb.PayMyBuddy.DTO.PaymentDTO;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.constants.TransactionType;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.security.jwt.JwtUtils;
import com.pmb.PayMyBuddy.service.AccountDetailsService;
import com.pmb.PayMyBuddy.service.IContactService;
import com.pmb.PayMyBuddy.service.IPaymentService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JsonTestMapper.class, TestSecurityConfig.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private IPaymentService paymentService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private AccountDetailsService accountDetailsService;
    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private WebApplicationContext context;

    private static List<TransactionDTO> transactionsDTO = new ArrayList<>();

    static {
        transactionsDTO.add(new TransactionDTO("john doe", LocalDateTime.now().minusDays(1), "debit", 20, OperationType.DEBIT, TransactionType.PAYMENT));
        transactionsDTO.add(new TransactionDTO("thomas doe", LocalDateTime.now(), "credit", 20, OperationType.CREDIT, TransactionType.PAYMENT));
    }

    @BeforeEach
    void setup() {

        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Tag("getAllPayments")
    void testGetAllPayments_shouldReturnListOfPaymentDTO() throws Exception {
        //ARRANGE
        when(paymentService.getAllPayments()).thenReturn(transactionsDTO);
        //ACT //ASSERT
        mvc.perform(get("/payment/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$['message']").value("all retrieved"))
                .andExpect(jsonPath("$['statusCode']").value(200))
                .andExpect(jsonPath("$.data['transactions'].[0].amount").value(transactionsDTO.get(0).getAmount()))
                .andExpect(jsonPath("$.data['transactions'].[0].name").value(transactionsDTO.get(0).getName()))
                .andExpect(jsonPath("$.data['transactions'].[1].description").value(transactionsDTO.get(1).getDescription()));
    }
    @Test
    @Tag("doPayment")
    void testDoPayment_shouldReturnTrue() throws Exception {
        //ARRANGE
      PaymentDTO  paymentDTO = new PaymentDTO("doe@exemple.fr", 20, "movie");
        when(paymentService.doPayment(any())).thenReturn(true);
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
    void testDoPayment_withPoorBalanceShouldReturnStatusConflict() throws Exception {
        //ARRANGE
        PaymentDTO  paymentDTO = new PaymentDTO("doe@exemple.fr", 20, "movie");
        when(paymentService.doPayment(any())).thenThrow(InsufficientFundsException.class);
        //ACT //ASSERT
        mvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonTestMapper.asJsonString(paymentDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isConflict());
    }

}
