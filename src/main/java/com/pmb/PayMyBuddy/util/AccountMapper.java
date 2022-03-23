package com.pmb.paymybuddy.util;

import com.pmb.paymybuddy.DTO.AccountDTO;
import com.pmb.paymybuddy.DTO.BankAccountDTO;
import com.pmb.paymybuddy.DTO.ContactDTO;
import com.pmb.paymybuddy.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {


    public AccountDTO toAccountDTO(Account account) {
        return new AccountDTO(account.getAccountOwner().getFirstName(), account.getAccountOwner().getLastName(),
                account.getAccountOwner().getBirthDate(),
                account.getMail(), account.getPassword(), account.getBalance());

    }
    public ContactDTO toContactDTO(Account account){
        return new ContactDTO(account.getAccountOwner().getFirstName(),account.getAccountOwner().getLastName(), account.getMail());
    }
}
