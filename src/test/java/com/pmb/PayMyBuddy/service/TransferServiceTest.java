package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.constants.OperationType;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private Transfer transfer;
    private TransactionDTO transactionDTO;
    private TransferDTO transferDTO;
    private Account account;
    private Account pMBAccount;
    private BankAccount bankAccount;
    private AppUser user;

    @BeforeEach
    void setUp() throws Exception {

        Role role = new Role(1L, "USER");
        account = new Account(2, "john@exemple.fr", "password", 0, true,
                role);
        pMBAccount = new Account();
        pMBAccount.setBalance(0);
        bankAccount = new BankAccount();
        user = new AppUser("john", "doe", LocalDate.now().minusYears(25));
        account.setAccountOwner(user);
        transferDTO = new TransferDTO(20, "cinéma", OperationType.CREDIT);
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

    //@Test
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

  //  @Test
    @Tag("doTransfer")
    @DisplayName("transfer for reload account should return true ")
    void doTransfer_Test_forReload_shouldReturnTrue() throws DataNotFoundException, InsufficientFundsException {
        //ACT //ASSERT
        user.setBankAccount(bankAccount);
        account.setBalance(50);
        when(accountRepository.findByMail(any(String.class))).thenReturn(Optional.of(account));
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(calculator.feeCalculator(20)).thenReturn(0.1);
        when(calculator.totalCalculator(20)).thenReturn(20.1);
        when(accountRepository.findById(1L)).thenReturn( Optional.of(pMBAccount));
        when(transferRepository.save(any(Transfer.class))).thenReturn(any());
      //  when(accountRepository.save()).thenReturn( Optional.of(pMBAccount));
        //ACT
        boolean result = transferService.doTransfer(transferDTO);
        //ASSERT
        assertTrue(result);
        assertThat(account.getBalance()).isEqualTo(50+20);
        verify(accountRepository).findByMail(any(String.class));
        verify(principalUser).getCurrentUserMail();
    }
}
