package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.PaymentDTO;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.constants.TransactionType;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.model.*;
import com.pmb.PayMyBuddy.repository.AccountRepository;
import com.pmb.PayMyBuddy.repository.PaymentRepository;
import com.pmb.PayMyBuddy.repository.TransferRepository;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.util.Calculator;
import com.pmb.PayMyBuddy.util.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private static PaymentRepository paymentRepository;
    @Mock
    private static AccountRepository accountRepository;
    @Mock
    private static TransactionMapper transactionMapper;
    @Mock
    private static PrincipalUser principalUser;
    @Mock
    private static Calculator calculator;
    @InjectMocks
    private PaymentService paymentService;
    private PaymentDTO paymentDTO;
    private static Account accountOwner;
    private static Account accountContact;
    private Account pMBAccount;
    private AppUser userOwner;
    private AppUser userContact;

    private static List<Payment> paymentDebit = new ArrayList<>();
    static {
        paymentDebit.add(new Payment(20, 0.1, "Debit", LocalDateTime.now().minusDays(1), accountOwner, accountContact));
    }

    private static List<Payment> paymentCredit = new ArrayList<>();

    static {
        paymentCredit.add(new Payment(20, 0.1, "credit", LocalDateTime.now(),accountContact, accountOwner));
    }

    private static List<TransactionDTO> transactionsDTO = new ArrayList<>();

    static {
        transactionsDTO.add(new TransactionDTO("john doe", LocalDateTime.now().minusDays(1), "debit", 20, OperationType.DEBIT, TransactionType.PAYMENT));
        transactionsDTO.add(new TransactionDTO("thomas doe", LocalDateTime.now(), "credit", 20, OperationType.CREDIT, TransactionType.PAYMENT));
    }
    @BeforeEach
    void setUp() throws Exception {

        Role role = new Role(1L, "USER");
        accountOwner = new Account(2, "john@exemple.fr", "password", 0, true,
                role);
        accountContact = new Account(2, "doe@exemple.fr", "password", 0, true,
                role);
        userOwner = new AppUser("john", "doe", LocalDate.now().minusYears(25));
        accountOwner.setAccountOwner(userOwner);
        userContact = new AppUser("thomas", "doe", LocalDate.now().minusYears(25));
        accountContact.setAccountOwner(userContact);
        userOwner.setContacts(Set.of(userContact));
        pMBAccount = new Account();
        pMBAccount.setBalance(0);
        paymentDTO = new PaymentDTO("doe@exemple.fr", 20, "billet de train");
    }

    @Test
    @Tag("doPayment")
    @DisplayName(" payment with disabled account should return false")
    void doPayment_Test_withDisabledAccount_shouldReturnFalse() throws InsufficientFundsException {
        //ARRANGE
        accountOwner.setEnabled(false);
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(accountRepository.findByMail("john@exemple.fr")).thenReturn(Optional.of(accountOwner));
        when(accountRepository.findByMail("doe@exemple.fr")).thenReturn(Optional.of(accountContact));
        //ACT
        boolean result = paymentService.doPayment(paymentDTO);
        //ASSERT
        assertFalse(result);
    }

    @Test
    @Tag("doPayment")
    @DisplayName(" payment with disabled contact account should return false")
    void doPayment_Test_withDisabledContactAccount_shouldReturnFalse() throws InsufficientFundsException {
        //ARRANGE
        accountContact.setEnabled(false);
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(accountRepository.findByMail("john@exemple.fr")).thenReturn(Optional.of(accountOwner));
        when(accountRepository.findByMail("doe@exemple.fr")).thenReturn(Optional.of(accountContact));
        //ACT
        boolean result = paymentService.doPayment(paymentDTO);
        //ASSERT
        assertFalse(result);
    }

    @Test
    @Tag("doPayment")
    @DisplayName(" payment for user non in contacts account should return false")
    void doPayment_Test_withUserNonInContacts_shouldReturnFalse() throws InsufficientFundsException {

        //ARRANGE
        userOwner.setContacts(Set.of());
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(accountRepository.findByMail("john@exemple.fr")).thenReturn(Optional.of(accountOwner));
        when(accountRepository.findByMail("doe@exemple.fr")).thenReturn(Optional.of(accountContact));
        //ACT
        boolean result = paymentService.doPayment(paymentDTO);
        //ASSERT
        assertFalse(result);
    }
    @Test
    @Tag("doPayment")
    @DisplayName(" payment for Insufficient funds should throw InsufficientFundsException")
    void doPayment_Test_withInsufficient_Funds_shouldThrowInsufficientFundsException() throws InsufficientFundsException {
        //ARRANGE
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(accountRepository.findByMail("john@exemple.fr")).thenReturn(Optional.of(accountOwner));
        when(accountRepository.findByMail("doe@exemple.fr")).thenReturn(Optional.of(accountContact));
        when(calculator.feeCalculator(20)).thenReturn(0.1);
        when(calculator.totalCalculator(20)).thenReturn(20.1);
        //ACT//ASSERT
        assertThrows(InsufficientFundsException.class, () -> paymentService.doPayment(paymentDTO));

    }
    @Test
    @Tag("doPayment")
    @DisplayName(" payment should return True")
    void doPayment_Test_shouldReturnTrue() throws InsufficientFundsException {
        //ARRANGE
        accountOwner.setBalance(50);
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(accountRepository.findByMail("john@exemple.fr")).thenReturn(Optional.of(accountOwner));
        when(accountRepository.findByMail("doe@exemple.fr")).thenReturn(Optional.of(accountContact));
        when(calculator.feeCalculator(20)).thenReturn(0.1);
        when(calculator.totalCalculator(20)).thenReturn(20.1);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(pMBAccount));
        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentDebit.get(0));
        when(accountRepository.save(any(Account.class))).thenReturn(pMBAccount);
        //ACT
        boolean result = paymentService.doPayment(paymentDTO);

        //ASSERT
        assertTrue(result);
        assertThat (accountOwner.getBalance()).isEqualTo(29.9);
        assertThat (accountContact.getBalance()).isEqualTo(20);
        assertThat(pMBAccount.getBalance()).isEqualTo(0.1);
    }

    @Test
    @Tag("getAllTransfers")
    @DisplayName("getAllTransfers test should return list of transaction DTO  ")
    void getAllTransfers_Test_shouldReturnListOfTransactionDTO() throws DataNotFoundException, InsufficientFundsException {

        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(paymentRepository.findByDebitAccount("john@exemple.fr")).thenReturn(paymentDebit);
        when(paymentRepository.findByCreditAccount("john@exemple.fr")).thenReturn(paymentCredit);
        doReturn(transactionsDTO.get(0)).when(transactionMapper).PaymentMapper(any(Payment.class), eq(OperationType.DEBIT));
        doReturn(transactionsDTO.get(1)).when(transactionMapper).PaymentMapper(any(Payment.class), eq(OperationType.CREDIT));
        //ACT
        List<TransactionDTO> result = paymentService.getAllPayments();
        //ASSERT
        assertThat(result.size()).isEqualTo(2);
        verify(principalUser,times(2)).getCurrentUserMail();
        verify(paymentRepository).findByCreditAccount("john@exemple.fr");
        verify(paymentRepository).findByDebitAccount("john@exemple.fr");
        verify(transactionMapper,times(2)).PaymentMapper(any(Payment.class), any());
    }
}
