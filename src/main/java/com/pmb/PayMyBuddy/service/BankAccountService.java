package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

import com.pmb.PayMyBuddy.model.BankAccount;
import com.pmb.PayMyBuddy.model.User;
import com.pmb.PayMyBuddy.repository.BankAccountRepository;
import com.pmb.PayMyBuddy.repository.UserRepository;
import com.pmb.PayMyBuddy.util.BankAccountMapper;
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
     * @throws DataNotFoundException if not found
     */
    @Override
    public BankAccountDTO getBankAccount(String email) throws DataNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findByOwner_Account_Mail(email).
                orElseThrow(() -> new DataNotFoundException("no bank account attached to this account "));
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
