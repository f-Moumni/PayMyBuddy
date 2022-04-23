package com.pmb.PayMyBuddy.util;

import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.model.AppUser;
import com.pmb.PayMyBuddy.model.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BankAccountMapperTest {

private BankAccountMapper bankAccountMapper;
private  BankAccount bankAccount;
    @BeforeEach
    void setUp() throws Exception {
        bankAccountMapper = new BankAccountMapper();

      bankAccount = new BankAccount("iban123","Swift123",new AppUser());
    }

    @Test
    @Tag("BankAccountMapper")
    @DisplayName("toBankAccountDTO  test should map BankAccount to BankAccountDTO ")
    void toProfileDTO_Test_shouldReturnProfileDTO() {
        //ARRANGE
        BankAccountDTO bankAccountDTO = new BankAccountDTO("iban123","Swift123");
        //ACT
        BankAccountDTO result = bankAccountMapper.toBankAccountDTO(bankAccount);
        //ASSERT
        assertThat(result).isEqualToComparingFieldByField(bankAccountDTO);
    }

}
