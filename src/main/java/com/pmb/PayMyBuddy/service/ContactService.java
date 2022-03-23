package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.ContactDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.User;
import com.pmb.PayMyBuddy.repository.AccountRepository;
import com.pmb.PayMyBuddy.repository.UserRepository;
import com.pmb.PayMyBuddy.util.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
public class ContactService implements IContactService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountMapper accountMapper;


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
        User user = userRepository.findByAccount_Mail(email).orElseThrow(() ->
                new DataNotFoundException("no account found with email address " + email));
        Account contact = user.getAccount();
        return accountMapper.toContactDTO(contact);
    }

    /**
     * add contact between two users
     *
     * @param contactDTO
     * @return true if added, false if not
     * @throws DataNotFoundException
     */

    @Override
    public boolean addContact(ContactDTO contactDTO, String ownerEmail) throws AlreadyExistsException {
        String email = contactDTO.getEmail();
        log.info("adding contact with email {}", email);
        User owner = userRepository.findByAccount_Mail(ownerEmail).get();
        User contact = userRepository.findByAccount_Mail(email).get();
        if (owner.getContacts().contains(contact)){
            log.error("{} is already in contact",contactDTO.getEmail());
            throw new AlreadyExistsException("contact already saved");
        }
        owner.addContact(contact);
        userRepository.save(owner);
        return true;
    }


    /**
     * delete contact between two users
     *
     * @param contactDTO
     * @param ownerEmail
     * @return true if deleted, false if not
     */

    @Override
    public boolean deleteContact(ContactDTO contactDTO, String ownerEmail) {
        String email = contactDTO.getEmail();
        log.info("adding contact with email {}", email);
        User owner = userRepository.findByAccount_Mail(ownerEmail).get();
        User contact = userRepository.findByAccount_Mail(email).get();
        owner.removeContact(contact);
        userRepository.save(owner);
        return true;
    }

    /* to do get all contacts*/
}


