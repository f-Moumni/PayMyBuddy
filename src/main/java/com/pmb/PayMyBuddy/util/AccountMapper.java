package com.pmb.PayMyBuddy.util;


import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.ContactDTO;
import com.pmb.PayMyBuddy.model.Account;
import org.springframework.stereotype.Component;

/**
 * account mapper
 */
@Component
public class AccountMapper {

    /**
     * toProfileDTO map account to profile DTO
     * @param account
     * @return profileDTO
     */
    public ProfileDTO toProfileDTO(Account account) {
        return new ProfileDTO(account.getAccountOwner().getFirstName(), account.getAccountOwner().getLastName(),
                account.getAccountOwner().getBirthDate(),account.getMail(), account.getBalance());

    }

    /**
     * toContactDTO map account to contactDTO
     * @param account
     * @return contactDTO
     */
    public ContactDTO toContactDTO(Account account){
        return new ContactDTO(account.getAccountOwner().getFirstName(),account.getAccountOwner().getLastName(), account.getMail());
    }
}
