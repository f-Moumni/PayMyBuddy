package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

public interface IBankAccountService {
    BankAccountDTO getBankAccount() throws DataNotFoundException;

    boolean addBankAccount(BankAccountDTO bankAccountToAdd);

    boolean updateBankAccount(BankAccountDTO bankAccountToAdd);
}
