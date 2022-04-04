package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;

import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {


    Boolean deleteUser(String mail) throws DataNotFoundException, InsufficientFundsException;

    ProfileDTO addUser(SignupDTO newSignupDTO) throws AlreadyExistsException;

    ProfileDTO updateUser(SignupDTO userToUpdate) ;

    ProfileDTO getUser(String mail) throws DataNotFoundException;
}
