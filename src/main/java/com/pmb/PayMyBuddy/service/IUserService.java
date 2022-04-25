package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;

import com.pmb.PayMyBuddy.exceptions.BalanceNotEmptyException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {


    Boolean deleteUser() throws DataNotFoundException, BalanceNotEmptyException;

    ProfileDTO addUser(SignupDTO newUser) throws AlreadyExistsException;

    ProfileDTO updateUser(SignupDTO userToUpdate);

    ProfileDTO getUser() throws DataNotFoundException;
}
