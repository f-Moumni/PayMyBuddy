package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.ContactDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface IContactService {
    ContactDTO getContact(String email) throws DataNotFoundException;

    boolean addContact(ContactDTO contactDTO, String mail) throws AlreadyExistsException;

    boolean deleteContact(ContactDTO contactDTO, String ownerEmail);
}
