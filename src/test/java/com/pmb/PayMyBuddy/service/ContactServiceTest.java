package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.ContactDTO;
import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.BalanceNotEmptyException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.AppUser;
import com.pmb.PayMyBuddy.model.Role;
import com.pmb.PayMyBuddy.repository.AccountRepository;
import com.pmb.PayMyBuddy.repository.UserRepository;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.util.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
    @Mock
    private static AccountRepository accountRepository;
    @Mock
    private static UserRepository userRepository;
    @Mock
    private static AccountMapper accountMapper;
    @Mock
    private static PrincipalUser principalUser;
    @InjectMocks
    private ContactService contactService;

    private AppUser userOwner;
    private AppUser userContact;
    private Account accountOwner;
    private Account accountContact;


    @BeforeEach
    void setUp() throws Exception {

        Role role = new Role(1L, "USER");
        accountOwner = new Account(2, "john@exemple.fr", "password", 0, true,
                role);
        accountContact = new Account(2, "doe@exemple.fr", "password", 0, true,
                role);
        userOwner = new AppUser("john", "doe", LocalDate.now().minusYears(25));
        userOwner.setAccount(accountOwner);
        userContact = new AppUser("thomas", "doe", LocalDate.now().minusYears(25));
        userContact.setAccount(accountContact);

    }

    @Test
    @Tag("addContact")
    @DisplayName(" add account owner to contacts list should throw IllegalArgumentException")
    void addContact_Test_shouldThrowIllegalArgumentException() {
        //ARRANGE
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.of(userOwner));
        //ACT//ASSERT
        assertThrows(IllegalArgumentException.class, () -> contactService.addContact("john@exemple.fr"));
    }

    @Test
    @Tag("addContact")
    @DisplayName(" add non exising user to contacts list should throw DataNotFoundException")
    void addContact_Test_shouldThrowDataNotFoundException() {
        //ARRANGE
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.of(userOwner));
        when(userRepository.findByAccount_Mail("doe@exemple.fr")).thenReturn(Optional.empty());
        //ACT//ASSERT
        assertThrows(DataNotFoundException.class, () -> contactService.addContact("doe@exemple.fr"));
    }

    @Test
    @Tag("addContact")
    @DisplayName(" add user already added to contacts list should throw AlreadyExistsException")
    void addContact_Test_shouldThrowAlreadyExistsException() {
        //ARRANGE
        userOwner.setContacts(List.of(userContact));
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.of(userOwner));
        when(userRepository.findByAccount_Mail("doe@exemple.fr")).thenReturn(Optional.of(userContact));
        //ACT//ASSERT
        assertThrows(AlreadyExistsException.class, () -> contactService.addContact("doe@exemple.fr"));
    }

   @Test
    @Tag("addContact")
    @DisplayName(" add user to contacts list should return ContactDTO")
    void addContact_Test_shouldReturnContactDTO() throws DataNotFoundException, AlreadyExistsException {
        //ARRANGE
        ContactDTO contact = new ContactDTO("thomas", "doe", "doe@exemple.fr");
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.of(userOwner));
        when(userRepository.findByAccount_Mail("doe@exemple.fr")).thenReturn(Optional.of(userContact));
        doReturn(userOwner).when(userRepository).save(userOwner);
        when(accountMapper.toContactDTO(userContact.getAccount())).thenReturn(contact);
        //ACT
        ContactDTO result = contactService.addContact("doe@exemple.fr");
        // ASSERT
       assertTrue(userOwner.getContacts().contains(userContact));
       assertThat(result).isEqualToComparingFieldByField(contact);
    }
    @Test
    @Tag("deleteContact")
    @DisplayName(" delete contact should return true")
    void deleteContact_Test_shouldReturnTrue()  {
        //ARRANGE
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.of(userOwner));
        when(userRepository.findByAccount_Mail("doe@exemple.fr")).thenReturn(Optional.of(userContact));
        doReturn(userOwner).when(userRepository).save(userOwner);
        //ACT
        boolean result = contactService.deleteContact("doe@exemple.fr");
        // ASSERT
        assertTrue(result);
    }
    @Test
    @Tag("getContacts")
    @DisplayName(" getContacts should return set of ContactDTO")
    void getContacts_Test_shouldReturnSetOfContactDTO()  {
        //ARRANGE
        ContactDTO contact = new ContactDTO("thomas", "doe", "doe@exemple.fr");
        userOwner.setContacts(List.of(userContact));
        when(principalUser.getCurrentUserMail()).thenReturn("john@exemple.fr");
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.of(userOwner));
        when(accountMapper.toContactDTO(any(Account.class))).thenReturn(contact);
        //ACT
        Set<ContactDTO> result = contactService.getContacts();
        // ASSERT
        assertThat(result.size()).isEqualTo(1);

    }
}
