package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.BalanceNotEmptyException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.AppUser;

import com.pmb.PayMyBuddy.model.Role;
import com.pmb.PayMyBuddy.repository.RoleRepository;
import com.pmb.PayMyBuddy.repository.UserRepository;
import com.pmb.PayMyBuddy.security.PasswordEncoder;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.util.AccountMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private static UserRepository userRepository;
    @Mock
    private static AccountMapper accountMapper;
    @Mock
    private static BCryptPasswordEncoder passwordEncoder;
    @Mock
    private static RoleRepository roleRepository;
    @Mock
    private static PrincipalUser principalUser;

    @InjectMocks
    private UserService userService;
    private static AppUser user;
    private Account account;
    private Role role;
    private ProfileDTO profileDTO;
    private SignupDTO signupDTO;

    @BeforeAll
    static void beforeAll() {


    }

    @BeforeEach
    void setUp() throws Exception {

        role = new Role(1L, "USER");
        account = new Account(2, "john@exemple.fr", "password", 0, true,
                role);
        user = new AppUser(1, "john", "doe", LocalDate.now().minusYears(25), account);
        profileDTO = new ProfileDTO("john", "doe", LocalDate.now().minusYears(25), "john@exemple.fr", 0);
        signupDTO = new SignupDTO("john", "doe", LocalDate.now().minusYears(25), "john@exemple.fr", "password");
        when(principalUser.getCurrentUserMail()).thenReturn(account.getMail());
    }

    @Test
    @Tag("deleteUser")
    @DisplayName("delete user by email with empty balance should return true")
    void deleteUser_Test_shouldReturnTrue() throws DataNotFoundException, BalanceNotEmptyException {
        //ARRANGE
        user.getAccount().setBalance(0);
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(user.getUserID());
        //ACT
        boolean result = userService.deleteUser();
        //ASSERT
        assertThat(result).isTrue();
        verify(userRepository).findByAccount_Mail("john@exemple.fr");
        verify(userRepository).deleteById(user.getUserID());
    }

    @Test
    @Tag("deleteUser")
    @DisplayName("delete user by user email with a non-existent user should trow DataNotFoundException")
    void deleteUser_Test_shouldTrowDataNotFoundException() throws DataNotFoundException, BalanceNotEmptyException {
        //ARRANGE
        user.getAccount().setBalance(0);
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.empty());
        //ACT //ASSERT
        assertThrows(DataNotFoundException.class,
                () -> userService.deleteUser());
        verify(userRepository).findByAccount_Mail("john@exemple.fr");

    }

    @Test
    @Tag("deleteUser")
    @DisplayName("delete user by user email with a balance not empty should trow BalanceNotEmptyException")
    void deleteUser_Test_shouldTrowBalanceNotEmptyException() throws DataNotFoundException, BalanceNotEmptyException {
        //ARRANGE
        user.getAccount().setBalance(180);
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.of(user));
        //ACT //ASSERT
        assertThrows(BalanceNotEmptyException.class,
                () -> userService.deleteUser());
        verify(userRepository).findByAccount_Mail("john@exemple.fr");

    }

    @Test
    @Tag("AddUser")
    @DisplayName("add user should return profileDTO of user added")
    void addUser_Test_shouldReturnProfileDtOOfUser() throws AlreadyExistsException {
        //ARRANGE
        when(userRepository.findByAccount_Mail(signupDTO.getMail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signupDTO.getPassword())).thenReturn(any(String.class));
        when(roleRepository.findByName("USER")).thenReturn(role);
        when(userRepository.save(any(AppUser.class))).thenReturn(user);
        when(accountMapper.toProfileDTO(any(Account.class))).thenReturn(profileDTO);
        //ACT
        ProfileDTO result = userService.addUser(signupDTO);
        // ASSERT
        verify(userRepository).save(any());
        verify(userRepository).findByAccount_Mail(signupDTO.getMail());
        verify(passwordEncoder).encode(signupDTO.getPassword());
        verify(accountMapper).toProfileDTO(any(Account.class));
        assertThat(result).isEqualToComparingFieldByField(profileDTO);
    }

    @Test
    @Tag("AddUser")
    @DisplayName("add existing user should throw AlreadyExistsException")
    void addUser_Test_shouldThrowAlreadyExistsException() throws AlreadyExistsException {
        //ARRANGE
        when(userRepository.findByAccount_Mail(signupDTO.getMail())).thenReturn(Optional.of(user));
        //ACT// ASSERT
        assertThrows(AlreadyExistsException.class,
                () -> userService.addUser(signupDTO));
        verify(userRepository).findByAccount_Mail(signupDTO.getMail());

    }

    @Test
    @Tag("UpdateUser")
    @DisplayName("update user should return new user's profile DTO")
    void addUser_Test_shouldReturnNewProfileDTo()  {
        //ARRANGE
        when(userRepository.findByAccount_Mail(signupDTO.getMail())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(accountMapper.toProfileDTO(any(Account.class))).thenReturn(profileDTO);
        //ACT
        ProfileDTO result = userService.updateUser(signupDTO);
        //ASSERT
        verify(userRepository).save(any());
        verify(userRepository).findByAccount_Mail(signupDTO.getMail());

        verify(accountMapper).toProfileDTO(any(Account.class));

    }

}
