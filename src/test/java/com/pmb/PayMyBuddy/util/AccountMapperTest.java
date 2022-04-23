package com.pmb.PayMyBuddy.util;

import com.pmb.PayMyBuddy.DTO.ContactDTO;
import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.exceptions.BalanceNotEmptyException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.AppUser;
import com.pmb.PayMyBuddy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class AccountMapperTest {
   private AccountMapper accountMapper;
    private Account account;
    private AppUser user;

    @BeforeEach
    void setUp() throws Exception {
        accountMapper = new AccountMapper();
        account = new Account(2, "john@exemple.fr", "password", 0, true,
                new Role(1L,"USER"));
        user = new AppUser( "john", "doe", LocalDate.now().minusYears(25));
account.setAccountOwner(user);
    }

    @Test
    @Tag("toProfileDTO")
    @DisplayName("toProfileDTO test should map account to Profile DTO ")
    void toProfileDTO_Test_shouldReturnProfileDTO() {
        //ARRANGE
      ProfileDTO  profileDTO = new ProfileDTO("john", "doe", LocalDate.now().minusYears(25), "john@exemple.fr", 0);
        //ACT
        ProfileDTO result = accountMapper.toProfileDTO(account);
        //ASSERT
        assertThat(result).isEqualToComparingFieldByField(profileDTO);
    }

    @Test
    @Tag("toContactDTO")
    @DisplayName("toContactDTO test should map account to Contact DTO ")
    void toProfileDTO_Test_shouldReturnContactDTO() {
        //ARRANGE
        ContactDTO contactDTO = new ContactDTO("john", "doe",  "john@exemple.fr");
        //ACT
        ContactDTO result = accountMapper.toContactDTO(account);
        //ASSERT
        assertThat(result).isEqualToComparingFieldByField(contactDTO);

    }
}
