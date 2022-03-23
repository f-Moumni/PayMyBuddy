package com.pmb.PayMyBuddy.util;


import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.model.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    public BankAccountDTO toBankAccountDTO (BankAccount account){

        return new BankAccountDTO(account.getIban(), account.getSwift());
    }
}
