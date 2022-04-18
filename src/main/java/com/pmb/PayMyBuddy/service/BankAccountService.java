package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.BankAccountDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

import com.pmb.PayMyBuddy.model.AppUser;
import com.pmb.PayMyBuddy.model.BankAccount;
import com.pmb.PayMyBuddy.repository.BankAccountRepository;
import com.pmb.PayMyBuddy.repository.UserRepository;
import com.pmb.PayMyBuddy.security.PrincipalUser;
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
     * @return bank account
     * @throws DataNotFoundException if not found
     */
    @Override
    public BankAccountDTO getBankAccount() throws DataNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findByOwner_Account_Mail(PrincipalUser.getCurrentUserMail()).
                orElseThrow(() -> new DataNotFoundException("no bank account attached to this account "));
        return bankAccountMapper.toBankAccountDTO(bankAccount);
    }

    @Override
    public boolean addBankAccount(BankAccountDTO bankAccountToAdd) {
        AppUser owner = userRepository.findByAccount_Mail(PrincipalUser.getCurrentUserMail()).get();
        // bankAccountRepository.findByOwner_Account_Mail(email).ifPresentOrElse();
        BankAccount newBankAccount = new BankAccount(bankAccountToAdd.getIban(), bankAccountToAdd.getSwift(), owner);
        bankAccountRepository.save(newBankAccount);
        return true;
    }
    @Override
    public boolean updateBankAccount(BankAccountDTO bankAccountToAdd) {
        AppUser owner = userRepository.findByAccount_Mail(PrincipalUser.getCurrentUserMail()).get();
        BankAccount bankAccount =owner.getBankAccount();
        bankAccount.setIban(bankAccountToAdd.getIban());
        bankAccount.setSwift(bankAccountToAdd.getSwift());
        bankAccountRepository.save(bankAccount);
        return true;
    }

}
