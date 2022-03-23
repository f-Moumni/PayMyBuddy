package com.pmb.paymybuddy.util;

import com.pmb.paymybuddy.DTO.BankAccountDTO;
import com.pmb.paymybuddy.model.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    public BankAccountDTO toBankAccountDTO (BankAccount account){

        return new BankAccountDTO(account.getIban(), account.getSwift());
    }
}
