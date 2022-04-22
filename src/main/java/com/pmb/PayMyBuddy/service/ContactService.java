package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.ContactDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.AppUser;
import com.pmb.PayMyBuddy.repository.AccountRepository;
import com.pmb.PayMyBuddy.repository.UserRepository;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.util.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ContactService implements IContactService {

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final AccountMapper accountMapper;

    private final PrincipalUser principalUser;

    @Autowired
    public ContactService(AccountRepository accountRepository, UserRepository userRepository, AccountMapper accountMapper, PrincipalUser principalUser) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
        this.principalUser = principalUser;
    }

    /**
     * searcher a contact by email
     *
     * @param email
     * @return
     * @throws DataNotFoundException
     */
    @Override
    public ContactDTO getContact(String email) throws DataNotFoundException {
        log.info("searching contact with email {}", email);
        AppUser appUser = userRepository.findByAccount_Mail(email).orElseThrow(() ->
                new DataNotFoundException("no account found with email address " + email));
        Account contact = appUser.getAccount();
        return accountMapper.toContactDTO(contact);
    }

    /**
     * add contact between two users
     *
     * @param
     * @return true if added, false if not
     * @throws DataNotFoundException
     */

    @Override
    public ContactDTO addContact(String contactMail) throws AlreadyExistsException, DataNotFoundException {

        log.info("adding contact with email {}", contactMail);
        AppUser owner = userRepository.findByAccount_Mail(principalUser.getCurrentUserMail()).get();
        AppUser contact = userRepository.findByAccount_Mail(contactMail).orElseThrow(() -> new DataNotFoundException("no account found with email address " + contactMail));
        if (owner.getContacts().contains(contact)) {
            log.error("{} is already in contact", contactMail);
            throw new AlreadyExistsException("contact already saved");
        }
        owner.addContact(contact);
        userRepository.save(owner);
        return accountMapper.toContactDTO(contact.getAccount());
    }

    /**
     * delete contact between two users
     *
     * @param
     * @return true if deleted, false if not
     */

    @Override
    public Boolean deleteContact(String contactMail) {
        log.info("adding contact with email {}", contactMail);
        AppUser owner = userRepository.findByAccount_Mail(principalUser.getCurrentUserMail()).get();
        AppUser contact = userRepository.findByAccount_Mail(contactMail).get();
        owner.removeContact(contact);
        userRepository.save(owner);
        return true;
    }


    @Override
    public Set<ContactDTO> getContacts() {
        log.info("getting all contacts of email {}", principalUser.getCurrentUserMail());
        AppUser owner = userRepository.findByAccount_Mail(principalUser.getCurrentUserMail()).get();
        return owner.getContacts().stream().map(user -> accountMapper.toContactDTO(user.getAccount())).collect(Collectors.toSet());
    }
}


