package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.constants.TransactionType;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
@Mock
    private static   PaymentService paymentService;
@Mock
    private static  TransferService transferService;
@InjectMocks
private TransactionService transactionService;

    private static List<TransactionDTO> payments = new ArrayList<>();

    static {
        payments.add(new TransactionDTO("john doe", LocalDateTime.now().minusDays(1), "debit", 20, OperationType.DEBIT, TransactionType.PAYMENT));
        payments.add(new TransactionDTO("thomas doe", LocalDateTime.now(), "credit", 20, OperationType.CREDIT, TransactionType.PAYMENT));
    }
    private static List<TransactionDTO> transfers = new ArrayList<>();

    static {
        transfers.add(new TransactionDTO("iban 123456", LocalDateTime.now().minusDays(1), "movie", 20, OperationType.DEBIT, TransactionType.TRANSFER));
        transfers.add(new TransactionDTO("iban 123456", LocalDateTime.now(), "facture", 22, OperationType.CREDIT, TransactionType.TRANSFER));
    }

    @Test
    @Tag("getAllTransactions")
    @DisplayName("get all transactions should return list of transaction DTO ")
    void getAllTransactions_Test_shouldReturnListOfTransactionDTO() {
        //ARRANGE
      when(paymentService.getAllPayments()).thenReturn(payments);
      when(transactionService.getAllTransactions()).thenReturn(transfers);
        //ACT
        List<TransactionDTO> result =transactionService.getAllTransactions();
        // ASSERT
       assertThat(result.size()).isEqualTo(4);
       assertThat(result.containsAll(transfers));
        assertThat(result.containsAll(payments));
    }
    @Test
    @Tag("getAllTransactions")
    @DisplayName("get all transactions for empty list should return list empty list")
    void getAllTransactions_Test_shouldReturnEmptyList() {
        //ARRANGE
        when(paymentService.getAllPayments()).thenReturn(List.of());
        when(transactionService.getAllTransactions()).thenReturn(List.of());
        //ACT
        List<TransactionDTO> result =transactionService.getAllTransactions();
        // ASSERT
        assertThat(result.size()).isEqualTo(0);
     result.isEmpty();
    }
}
