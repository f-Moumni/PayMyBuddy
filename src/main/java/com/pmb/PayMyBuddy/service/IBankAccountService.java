package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

public interface IBankAccountService {
    BankAccountDTO getBankAccount(String email) throws DataNotFoundException;

    boolean addBankAccount(BankAccountDTO bankAccountToAdd, String email);
}
