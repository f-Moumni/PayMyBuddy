package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.DTO.AccountDTO;
import com.pmb.paymybuddy.exception.AlreadyExistsException;
import com.pmb.paymybuddy.exception.BalanceException;
import com.pmb.paymybuddy.exception.DataNoteFoundException;
import com.pmb.paymybuddy.model.User;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {


    Boolean deleteUser(AccountDTO accountToDelete) throws DataNoteFoundException, BalanceException;

    AccountDTO addUser(AccountDTO accountDTO) throws AlreadyExistsException;

    AccountDTO updateUser(AccountDTO accountToUpdate) throws DataNoteFoundException;

    AccountDTO getUser(String mail) throws DataNoteFoundException;
}
