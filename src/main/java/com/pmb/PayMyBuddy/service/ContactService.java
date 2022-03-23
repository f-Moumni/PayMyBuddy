package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.DTO.ContactDTO;
import com.pmb.paymybuddy.exception.AlreadyExistsException;
import com.pmb.paymybuddy.exception.DataNoteFoundException;
import com.pmb.paymybuddy.model.Account;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.repository.AccountRepository;
import com.pmb.paymybuddy.repository.UserRepository;
import com.pmb.paymybuddy.util.AccountMapper;
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
     * @throws DataNoteFoundException
     */
    @Override
    public ContactDTO getContact(String email) throws DataNoteFoundException {
        log.info("searching contact with email {}", email);
        User user = userRepository.findByAccount_Mail(email).orElseThrow(() ->
                new DataNoteFoundException("no account found with email address " + email));
        Account contact = user.getAccount();
        return accountMapper.toContactDTO(contact);
    }

    /**
     * add contact between two users
     *
     * @param contactDTO
     * @return true if added, false if not
     * @throws DataNoteFoundException
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


