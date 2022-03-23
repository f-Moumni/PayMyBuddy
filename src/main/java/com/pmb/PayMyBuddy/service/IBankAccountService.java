package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.DTO.BankAccountDTO;
import com.pmb.paymybuddy.exception.DataNoteFoundException;

public interface IBankAccountService {
    BankAccountDTO getBankAccount(String email) throws DataNoteFoundException;

    boolean addBankAccount(BankAccountDTO bankAccountToAdd, String email);
}
