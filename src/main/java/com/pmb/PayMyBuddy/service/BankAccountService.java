package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.DTO.BankAccountDTO;
import com.pmb.paymybuddy.exception.DataNoteFoundException;
import com.pmb.paymybuddy.model.BankAccount;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.repository.BankAccountRepository;
import com.pmb.paymybuddy.repository.UserRepository;
import com.pmb.paymybuddy.util.BankAccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
public class BankAccountService implements IBankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BankAccountMapper bankAccountMapper;

    /**
     * get a bank account for a given account email
     *
     * @param email account email
     * @return bank account
     * @throws DataNoteFoundException if not found
     */
    @Override
    public BankAccountDTO getBankAccount(String email) throws DataNoteFoundException {
        BankAccount bankAccount = bankAccountRepository.findByOwner_Account_Mail(email).
                orElseThrow(() -> new DataNoteFoundException("no bank account attached to this account "));
        return bankAccountMapper.toBankAccountDTO(bankAccount);
    }

    @Override
    public boolean addBankAccount(BankAccountDTO bankAccountToAdd, String email) {
        User owner = userRepository.findByAccount_Mail(email).get();
        // bankAccountRepository.findByOwner_Account_Mail(email).ifPresentOrElse();
        BankAccount newBankAccount = new BankAccount(bankAccountToAdd.getIban(), bankAccountToAdd.getSwift(), owner);
        bankAccountRepository.save(newBankAccount);
        return true;
    }
}
