package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.DTO.ContactDTO;
import com.pmb.paymybuddy.exception.AlreadyExistsException;
import com.pmb.paymybuddy.exception.DataNoteFoundException;
import org.springframework.stereotype.Service;

@Service
public interface IContactService {
    ContactDTO getContact(String email) throws DataNoteFoundException;

    //to do : add contact to contact's list
    boolean addContact(ContactDTO contactDTO, String mail) throws AlreadyExistsException;

    boolean deleteContact(ContactDTO contactDTO, String ownerEmail);
}
