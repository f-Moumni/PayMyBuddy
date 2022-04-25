package com.pmb.PayMyBuddy.util;

import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.constants.TransactionType;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
public class TransactionMapperTest {
    private  TransactionMapper transactionMapper;
    private static BankAccount bankAccount;
    private AppUser userOwner;
    private AppUser userContact;
    private static Account accountOwner;
    private static Account accountContact;



    @BeforeEach
    void setUp() throws Exception {
        transactionMapper = new TransactionMapper();
        accountOwner = new Account(2, "john@exemple.fr", "password", 0, true,
                new Role(1L,"USER"));
        accountContact = new Account(2, "doe@exemple.fr", "password", 0, true,
                new Role(1L,"USER"));
        userOwner = new AppUser("john", "doe", LocalDate.now().minusYears(25));
        accountOwner.setAccountOwner(userOwner);
        userContact = new AppUser("thomas", "doe", LocalDate.now().minusYears(25));
    accountContact.setAccountOwner(userContact);
        bankAccount = new BankAccount("iban 123456","swift123",userOwner);

    }

    @Test
    @Tag("transferMapper")
    @DisplayName("transferMapper should return TransactionDTO with Iban in name ")
    void transferMapper_Test_shouldReturnTransactionDTO() {
        //ARRANGE
         Transfer transfer = new Transfer(20, 0.1, "sport", LocalDateTime.now().minusHours(1), accountOwner, null, bankAccount);
       TransactionDTO transactionDTO= new TransactionDTO("iban 123456", LocalDateTime.now().minusHours(1), "sport", 20, OperationType.DEBIT, TransactionType.TRANSFER);
        //ACT
        TransactionDTO  result = transactionMapper.transferMapper(transfer,OperationType.DEBIT);
        // ASSERT
        assertThat(result.getName()).isEqualTo(transactionDTO.getName());
        assertThat(result).isEqualToComparingFieldByField(transactionDTO);

    }

    @Test
    @Tag("PaymentMapper")
    @DisplayName("PaymentMapper for disabled account should return TransactionDTO with email in name filed")
    void transferMapper_Test_withDisabledAccount_shouldReturnTransactionDTO() {
        //ARRANGE
        accountContact.setEnabled(false);
       Payment payment =new Payment(20, 0.1, "Sport", LocalDateTime.now().minusDays(1), accountOwner, accountContact);
      TransactionDTO transactionDTO = new TransactionDTO("doe@exemple.fr", LocalDateTime.now().minusDays(1), "Sport", 20, OperationType.DEBIT, TransactionType.PAYMENT);
        //ACT
        TransactionDTO result = transactionMapper.PaymentMapper(payment,OperationType.DEBIT);
        // ASSERT
        assertThat(result).isEqualToComparingFieldByField(transactionDTO);

    }
   @Test
    @Tag("PaymentMapper")
    @DisplayName("PaymentMapper for active account should return TransactionDTO with first and last name in name filed")
    void transferMapper_Test_withActiveAccount_shouldReturnTransactionDTO() {
        //ARRANGE
        Payment payment =new Payment(20, 0.1, "Sport", LocalDateTime.now().minusDays(1), accountOwner, accountContact);
        TransactionDTO transactionDTO = new TransactionDTO("thomas doe", LocalDateTime.now().minusDays(1), "Sport", 20, OperationType.DEBIT, TransactionType.PAYMENT);
        //ACT
        TransactionDTO result = transactionMapper.PaymentMapper(payment,OperationType.DEBIT);
        // ASSERT
        assertThat(result).isEqualToComparingFieldByField(transactionDTO);

    }
    @Test
    @Tag("PaymentMapper")
    @DisplayName("PaymentMapper for credit operation should return TransactionDTO with first and last's credit user in name filed")
    void transferMapper_Test_withOperationCredit_shouldReturnTransactionDTO() {
        //ARRANGE
        Payment payment =new Payment(20, 0.1, "Sport", LocalDateTime.now().minusDays(1), accountContact, accountOwner);
        TransactionDTO transactionDTO = new TransactionDTO("thomas doe", LocalDateTime.now().minusDays(1), "Sport", 20, OperationType.CREDIT, TransactionType.PAYMENT);
        //ACT
        TransactionDTO result = transactionMapper.PaymentMapper(payment,OperationType.CREDIT);
        // ASSERT
        assertThat(result).isEqualToComparingFieldByField(transactionDTO);

    }
}
