package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;

import com.pmb.PayMyBuddy.exceptions.BalanceNotEmptyException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import org.springframework.stereotype.Service;

/**
 * contain all business service methods for user
 */
@Service
public interface IUserService {

    /**
     *  delete current user
     * @return
     * @throws DataNotFoundException
     * @throws BalanceNotEmptyException
     */
    Boolean deleteUser() throws DataNotFoundException, BalanceNotEmptyException;

    /**
     * add new user
     * @param newUser
     * @return
     * @throws AlreadyExistsException
     */
    ProfileDTO addUser(SignupDTO newUser) throws AlreadyExistsException;

    /**
     * update account of current user
     * @param userToUpdate
     * @return
     */
    ProfileDTO updateUser(SignupDTO userToUpdate);

    /**
     * get information of current user
     * @return
     * @throws DataNotFoundException
     */
    ProfileDTO getUser() throws DataNotFoundException;
}
