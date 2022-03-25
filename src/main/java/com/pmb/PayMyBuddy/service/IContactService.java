package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.ContactDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface IContactService {
    ContactDTO getContact(String email) throws DataNotFoundException;



    boolean addContact(String contactMail, String ownerEmail) throws AlreadyExistsException;



     Boolean deleteContact(String contactMail, String ownerEmail);


    Set<ContactDTO> getContacts(String ownerEmail);
}
