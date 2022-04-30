package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

/**
 * IBankAccountService contain all business service methods for BankAccount
 */
public interface IBankAccountService {

    /**
     * get BankAccount of current user
     * @return BankAccountDTO
     * @throws DataNotFoundException
     */
     BankAccountDTO getBankAccount() throws DataNotFoundException;

    /**
     * save new BankAccount for current User
     * @param bankAccountToAdd
     * @return true if is done
     */
    boolean addBankAccount(BankAccountDTO bankAccountToAdd);

    /**
     * Update BankAccount of current User
     * @param bankAccountToAdd
     * @return true if is done
     */
    boolean updateBankAccount(BankAccountDTO bankAccountToAdd);
}
