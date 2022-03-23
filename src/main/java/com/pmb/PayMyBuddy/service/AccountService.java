package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.AccountDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.repository.AccountRepository;
import com.pmb.PayMyBuddy.util.AccountMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j

public class AccountService implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    /**
     * get all account information for profile
     *
     * @param mail
     * @return
     * @throws DataNotFoundException
     */
    @Override
    public AccountDTO getAccount(String mail) throws DataNotFoundException {
        log.info("getting account with email {}", mail);
        Account account = accountRepository.findByMail(mail).orElseThrow(() ->
                new DataNotFoundException("no account found with email address " + mail));

        return accountMapper.toAccountDTO(account);

    }


}
