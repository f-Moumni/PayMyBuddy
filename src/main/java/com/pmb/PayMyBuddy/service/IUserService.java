package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.AccountDTO;
import com.pmb.PayMyBuddy.DTO.UserDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;

import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {




    Boolean deleteUser(String mail) throws DataNotFoundException, InsufficientFundsException;

    AccountDTO addUser(UserDTO newUserDTO) throws AlreadyExistsException;

    AccountDTO updateUser(UserDTO userToUpdate) ;

    AccountDTO getUser(String mail) throws DataNotFoundException;
}
