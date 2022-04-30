package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.ContactDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * contain all business service methods for contact
 */
@Service
public interface IContactService {

    /**
     * add contact between two users
     *
     * @param
     * @return true if added, false if not
     * @throws DataNotFoundException
     */
    ContactDTO addContact(String contactMail) throws AlreadyExistsException, DataNotFoundException;
    /**
     * delete contact between two users
     *
     * @param
     * @return true if deleted, false if not
     */
    Boolean deleteContact(String contactMail);

    /**
     * get all contacts of current User
     * @return
     */
    Set<ContactDTO> getContacts();
}
