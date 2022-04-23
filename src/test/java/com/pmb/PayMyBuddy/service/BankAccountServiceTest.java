package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.AppUser;
import com.pmb.PayMyBuddy.model.BankAccount;
import com.pmb.PayMyBuddy.repository.BankAccountRepository;
import com.pmb.PayMyBuddy.repository.UserRepository;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.util.BankAccountMapper;
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
public class BankAccountServiceTest {
    @Mock
    private static BankAccountRepository bankAccountRepository;
    @Mock
    private static UserRepository userRepository;
    @Mock
    private static BankAccountMapper bankAccountMapper;
    @Mock
    private static PrincipalUser principalUser;
    @InjectMocks
    private BankAccountService bankAccountService;

    private Account account;
    private BankAccount bankAccount;
    private AppUser user;
    private BankAccountDTO bankAccountDTO;

    @BeforeEach
    void setUp() throws Exception {
        account = new Account();
        account.setMail("john@exemple.fr");
        user = new AppUser("john", "doe", LocalDate.now().minusYears(25));
        account.setAccountOwner(user);
        bankAccount = new BankAccount("iban", "swift", user);
        bankAccountDTO = new BankAccountDTO("iban", "swift");

    }

   @Test
    @Tag("getBankAccount")
    @DisplayName("getBankAccount with an account without a bank account should throw DataNotFoundException ")
    void getBankAccount_Test_shouldThrowDataNotFoundException() {
       //ARRANGE
       when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
       when(bankAccountRepository.findByOwner_Account_Mail("john@exemple.fr")).thenReturn(Optional.empty());

       //ACT //ASSERT
         assertThrows(DataNotFoundException.class, () -> bankAccountService.getBankAccount());
       verify(bankAccountRepository).findByOwner_Account_Mail(any(String.class));
       verify(principalUser).getCurrentUserMail();
   }

    @Test
    @Tag("getBankAccount")
    @DisplayName("getBankAccount should return a BankAccountDTO")
    void getBankAccount_Test_shouldReturnABankAccountDTO() throws DataNotFoundException {
        //ARRANGE
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(bankAccountRepository.findByOwner_Account_Mail("john@exemple.fr")).thenReturn(Optional.of(bankAccount));
        when(bankAccountMapper.toBankAccountDTO(bankAccount)).thenReturn(bankAccountDTO);
        //ACT
        BankAccountDTO result = bankAccountService.getBankAccount();
        //ASSERT
        assertThat(result).isEqualToComparingFieldByField(bankAccountDTO);
        verify(bankAccountRepository).findByOwner_Account_Mail(any(String.class));
        verify(principalUser).getCurrentUserMail();
    }
    @Test
    @Tag("addBankAccount")
    @DisplayName("addBankAccount should return a True")
    void ddBankAccount_Test_shouldReturnTrue() {
        //ARRANGE
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.of(user));
      doReturn(bankAccount).when(bankAccountRepository).save(any(BankAccount.class));
        //ACT
        boolean result = bankAccountService.addBankAccount(bankAccountDTO);
        //ASSERT
        assertTrue(result);
        verify(userRepository).findByAccount_Mail(any(String.class));
        verify(principalUser).getCurrentUserMail();
    }

    @Test
    @Tag("updateBankAccount")
    @DisplayName("updateBankAccount should return a True")
    void updateBankAccount_Test_shouldReturnTrue() {
        //ARRANGE
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.of(user));
        doReturn(bankAccount).when(bankAccountRepository).save(any(BankAccount.class));
        //ACT
        boolean result = bankAccountService.addBankAccount(bankAccountDTO);
        //ASSERT
        assertTrue(result);
        verify(userRepository).findByAccount_Mail(any(String.class));
        verify(principalUser).getCurrentUserMail();
    }

}


