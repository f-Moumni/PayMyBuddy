package com.pmb.PayMyBuddy.controller;

import com.pmb.PayMyBuddy.DTO.PaymentDTO;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.constants.TransactionType;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.security.jwt.JwtUtils;
import com.pmb.PayMyBuddy.service.AccountDetailsService;
import com.pmb.PayMyBuddy.service.IPaymentService;
import com.pmb.PayMyBuddy.service.ITransferService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JsonTestMapper.class, TestSecurityConfig.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(TransferController.class)
public class TransferControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ITransferService transferService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private AccountDetailsService accountDetailsService;
    @MockBean
    private JwtUtils jwtUtils;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private static List<TransactionDTO> transactionsDTO = new ArrayList<>();

    static {
        transactionsDTO.add(new TransactionDTO("iban 123456", LocalDateTime.now().minusDays(1), "movie", 20, OperationType.DEBIT, TransactionType.TRANSFER));
        transactionsDTO.add(new TransactionDTO("iban 123456", LocalDateTime.now(), "facture", 22, OperationType.CREDIT, TransactionType.TRANSFER));

    }
    @Test
    @Tag("getAllTransfer")
    void testGetAllTransfer_shouldReturnListOfTransactionDTO() throws Exception {
        //ARRANGE
        when(transferService.getAllTransfers()).thenReturn(transactionsDTO);
        //ACT //ASSERT
        mvc.perform(get("/transfer/all")
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
    @Tag("doTransfer")
    void testDoTransfer_shouldReturnTrue() throws Exception {
        //ARRANGE
        TransferDTO transferDTO = new TransferDTO( 20, "movie",OperationType.DEBIT);
        when(transferService.doTransfer(any())).thenReturn(true);
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
    void testDoTransfer_withPoorBalanceShouldReturnStatusConflict() throws Exception {
        //ARRANGE
        TransferDTO transferDTO = new TransferDTO( 20, "movie",OperationType.DEBIT);
        when(transferService.doTransfer(any())).thenThrow(InsufficientFundsException.class);
        //ACT //ASSERT
        mvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonTestMapper.asJsonString(transferDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isConflict());

    }
    @Test
    @Tag("doTransfer")
    void testDoTransfer_withNonBankAccountShouldReturnNotFound() throws Exception {
        //ARRANGE
        TransferDTO transferDTO = new TransferDTO( 20, "movie",OperationType.DEBIT);
        when(transferService.doTransfer(any())).thenThrow(DataNotFoundException.class);
        //ACT //ASSERT
        mvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonTestMapper.asJsonString(transferDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound());

    }
}
