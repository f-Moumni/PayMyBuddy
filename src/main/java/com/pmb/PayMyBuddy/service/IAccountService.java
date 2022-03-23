package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.DTO.AccountDTO;
import com.pmb.paymybuddy.DTO.ContactDTO;
import com.pmb.paymybuddy.exception.AlreadyExistsException;
import com.pmb.paymybuddy.exception.DataNoteFoundException;

import org.springframework.stereotype.Service;

@Service
public interface IAccountService {


    AccountDTO getAccount(String mail) throws DataNoteFoundException;



}
