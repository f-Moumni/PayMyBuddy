package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.AppUser;
import com.pmb.PayMyBuddy.model.Role;
import com.pmb.PayMyBuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountDetailsServiceTest {
    @Mock
    private static UserRepository userRepository;
    @InjectMocks
    private AccountDetailsService accountDetailsService;
    private AppUser user;
    private Account account;
    private Role role;

    @BeforeEach
    void setUp() throws Exception {

        role = new Role(1L, "USER");
        account = new Account(2, "john@exemple.fr", "password", 0, true,
                role);
        user = new AppUser(1, "john", "doe", LocalDate.now().minusYears(25), account);
    }

    @Test
    @Tag("loadUserByUsername")
    @DisplayName("loadUserByUsername should return UserDetails of account for a given email")
    void loadUserByUsername_Test_shouldUserDetail()  {
       //ARRANGE
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.of(user));
        //ACT
        UserDetails userDetail =  accountDetailsService.loadUserByUsername("john@exemple.fr");
             //   ASSERT
        assertThat(userDetail.getUsername()).isEqualTo("john@exemple.fr");
        assertThat(userDetail.getPassword()).isEqualTo("password");
    }
@Test
    @Tag("loadUserByUsername")
    @DisplayName("loadUserByUsername for non registered User should throw UsernameNotFoundException")
    void loadUserByUsername_Test_shouldThrowUsernameNotFoundException()  {
        //ARRANGE
        when(userRepository.findByAccount_Mail("john@exemple.fr")).thenReturn(Optional.empty());
        //ACT
      assertThrows(UsernameNotFoundException.class,()->accountDetailsService.loadUserByUsername("john@exemple.fr"));

    }
}
