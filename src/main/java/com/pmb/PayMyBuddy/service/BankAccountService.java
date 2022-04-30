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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
/**
 * BankAccountService contain all business service methods for BankAccount
 */
@Service
@Transactional
@Slf4j
public class BankAccountService implements IBankAccountService {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountService.class);
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final BankAccountMapper bankAccountMapper;
    private final PrincipalUser principalUser;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository, BankAccountMapper bankAccountMapper, PrincipalUser principalUser) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.principalUser = principalUser;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BankAccountDTO getBankAccount() throws DataNotFoundException {
        logger.info("getting bank Account");
        BankAccount bankAccount = bankAccountRepository.findByOwner_Account_Mail(principalUser.getCurrentUserMail()).
                orElseThrow(() -> new DataNotFoundException("no bank account attached to this account "));
        return bankAccountMapper.toBankAccountDTO(bankAccount);
    }

    @Override
    public boolean addBankAccount(BankAccountDTO bankAccountToAdd) {
        logger.info("saving bank account");
        AppUser owner = userRepository.findByAccount_Mail(principalUser.getCurrentUserMail()).get();
        BankAccount newBankAccount = new BankAccount(bankAccountToAdd.getIban(), bankAccountToAdd.getSwift(), owner);
        bankAccountRepository.save(newBankAccount);
        return true;
    }

    @Override
    public boolean updateBankAccount(BankAccountDTO bankAccountToAdd) {
        logger.info("updating bank account");
        AppUser owner = userRepository.findByAccount_Mail(principalUser.getCurrentUserMail()).get();
        BankAccount bankAccount = owner.getBankAccount();
        bankAccount.setIban(bankAccountToAdd.getIban());
        bankAccount.setSwift(bankAccountToAdd.getSwift());
        bankAccountRepository.save(bankAccount);
        return true;
    }

}
