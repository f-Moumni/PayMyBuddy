package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.constants.TransactionType;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.model.*;
import com.pmb.PayMyBuddy.repository.AccountRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {
    @Mock
    private static TransferRepository transferRepository;
    @Mock
    private static AccountRepository accountRepository;
    @Mock
    private static TransactionMapper transactionMapper;
    @Mock
    private static PrincipalUser principalUser;
    @Mock
    private static Calculator calculator;
    @InjectMocks
    private TransferService transferService;

    private static TransferDTO transferDTO;
    private static Account account;
    private static Account pMBAccount;
    private static BankAccount bankAccount;
    private static AppUser user;

    private static List<Transfer> transfersDebit = new ArrayList<>();
    static {
        transfersDebit.add(new Transfer(20, 0.1, "sport", LocalDateTime.now().minusDays(1), account, null, bankAccount));
    }

    private static List<Transfer> transfersCredit = new ArrayList<>();

    static {
        transfersCredit.add(new Transfer(20, 0.1, "sport", LocalDateTime.now().minusDays(1), account, null, bankAccount));
    }

    private static List<TransactionDTO> transactionsDTO = new ArrayList<>();

    static {
        transactionsDTO.add(new TransactionDTO("iban 123456", LocalDateTime.now().minusDays(1), "movie", 20, OperationType.DEBIT, TransactionType.TRANSFER));
        transactionsDTO.add(new TransactionDTO("iban 123456", LocalDateTime.now(), "facture", 22, OperationType.CREDIT, TransactionType.TRANSFER));

    }

    @BeforeEach
    void setUp() throws Exception {
        account = new Account();
        account.setMail("john@exemple.fr");
        account.setBalance(0);
        pMBAccount = new Account();
        pMBAccount.setBalance(0);
        bankAccount = new BankAccount();
        user = new AppUser("john", "doe", LocalDate.now().minusYears(25));
        account.setAccountOwner(user);
        transferDTO = new TransferDTO(20, "cinéma", OperationType.DEBIT);
    }


    @Test
    @Tag("doTransfer")
    @DisplayName("transfer with an account without a bank account should throw DataNotFoundException ")
    void doTransfer_Test_shouldThrowDataNotFoundException() {
        //ARRANGE
        when(accountRepository.findByMail(any(String.class))).thenReturn(Optional.of(account));
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");

        //ACT //ASSERT
        assertThrows(DataNotFoundException.class, () -> transferService.doTransfer(transferDTO));
        verify(accountRepository).findByMail(any(String.class));
        verify(principalUser).getCurrentUserMail();

    }

    @Test
    @Tag("doTransfer")
    @DisplayName("transfer with a tight balance should Throw InsufficientFundsException ")
    void doTransfer_Test_shouldThrowInsufficientFundsException() {
        //ARRANGE
        user.setBankAccount(bankAccount);
        when(accountRepository.findByMail(any(String.class))).thenReturn(Optional.of(account));
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(calculator.feeCalculator(20)).thenReturn(0.1);
        when(calculator.totalCalculator(20)).thenReturn(20.1);
        //ACT //ASSERT
        assertThrows(InsufficientFundsException.class, () -> transferService.doTransfer(transferDTO));
        verify(accountRepository).findByMail(any(String.class));
        verify(principalUser).getCurrentUserMail();
    }

    @Test
    @Tag("doTransfer")
    @DisplayName("transfer for reload account should return true ")
    void doTransfer_Test_forReload_shouldReturnTrue() throws DataNotFoundException, InsufficientFundsException {
        //ARRANGE
        user.setBankAccount(bankAccount);
        account.setBalance(50);
        transferDTO.setOperationType(OperationType.CREDIT);
        when(accountRepository.findByMail(any(String.class))).thenReturn(Optional.of(account));
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(calculator.feeCalculator(20)).thenReturn(0.1);
        when(calculator.totalCalculator(20)).thenReturn(20.1);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(pMBAccount));
        when(transferRepository.save(any(Transfer.class))).thenReturn(transfersCredit.get(0));
        when(accountRepository.save(any(Account.class))).thenReturn(pMBAccount);
        //ACT
        boolean result = transferService.doTransfer(transferDTO);
        //ASSERT
        assertTrue(result);
        assertThat(account.getBalance()).isEqualTo(70);
        assertThat(pMBAccount.getBalance()).isEqualTo(0.1);
        verify(transferRepository).save(any(Transfer.class));
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(accountRepository).findByMail(any(String.class));
        verify(principalUser).getCurrentUserMail();

    }

    @Test
    @Tag("doTransfer")
    @DisplayName("transfer to bank account should return true ")
    void doTransfer_Test_forTransferTOBankAccount_shouldReturnTrue() throws DataNotFoundException, InsufficientFundsException {
        //ARRANGE
        user.setBankAccount(bankAccount);
        account.setBalance(50);
        when(accountRepository.findByMail(any(String.class))).thenReturn(Optional.of(account));
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(calculator.feeCalculator(20)).thenReturn(0.1);
        when(calculator.totalCalculator(20)).thenReturn(20.1);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(pMBAccount));
        when(transferRepository.save(any(Transfer.class))).thenReturn(transfersDebit.get(0));
        when(accountRepository.save(any(Account.class))).thenReturn(pMBAccount);
        //ACT
        boolean result = transferService.doTransfer(transferDTO);
        //ASSERT
        assertTrue(result);
        assertThat(account.getBalance()).isEqualTo(29.9);
        assertThat(pMBAccount.getBalance()).isEqualTo(0.1);
        verify(transferRepository).save(any(Transfer.class));
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(accountRepository).findByMail(any(String.class));
        verify(principalUser).getCurrentUserMail();
    }

    @Test
    @Tag("getAllTransfers")
    @DisplayName("getAllTransfers test should return list of transaction DTO  ")
    void getAllTransfers_Test_shouldReturnListOfTransactionDTO() throws DataNotFoundException, InsufficientFundsException {

        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(transferRepository.findByDebitAccount("john@exemple.fr")).thenReturn(transfersDebit);
        when(transferRepository.findByCreditAccount("john@exemple.fr")).thenReturn(transfersCredit);
        doReturn(transactionsDTO.get(0)).when(transactionMapper).transferMapper(any(Transfer.class), eq(OperationType.DEBIT));
        doReturn(transactionsDTO.get(1)).when(transactionMapper).transferMapper(any(Transfer.class), eq(OperationType.CREDIT));
        //ACT
        List<TransactionDTO> result = transferService.getAllTransfers();
        //ASSERT
        assertThat(result.size()).isEqualTo(2);
        verify(principalUser,times(2)).getCurrentUserMail();
        verify(transferRepository).findByCreditAccount("john@exemple.fr");
        verify(transferRepository).findByDebitAccount("john@exemple.fr");
        verify(transactionMapper,times(2)).transferMapper(any(Transfer.class), any());
    }

}
