package com.pmb.PayMyBuddy.util;


import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.model.BankAccount;
import org.springframework.stereotype.Component;

/**
 * bank account mapper
 */
@Component
public class BankAccountMapper {
    /**
     * toBankAccountDTO map bankAccount to BankAccountDTO
     * @param account BankAccount
     * @return BankAccountDTO
     */
    public BankAccountDTO toBankAccountDTO (BankAccount account){

        return new BankAccountDTO(account.getIban(), account.getSwift());
    }
}
