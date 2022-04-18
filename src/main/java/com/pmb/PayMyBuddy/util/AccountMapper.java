package com.pmb.PayMyBuddy.util;


import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.ContactDTO;
import com.pmb.PayMyBuddy.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {


    public ProfileDTO toProfileDTO(Account account) {
        return new ProfileDTO(account.getAccountOwner().getFirstName(), account.getAccountOwner().getLastName(),
                account.getAccountOwner().getBirthDate(),account.getMail(), account.getBalance());

    }
    public ContactDTO toContactDTO(Account account){
        return new ContactDTO(account.getAccountOwner().getFirstName(),account.getAccountOwner().getLastName(), account.getMail());
    }
}
