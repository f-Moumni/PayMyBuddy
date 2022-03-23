package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.DTO.AccountDTO;
import com.pmb.paymybuddy.DTO.ContactDTO;
import com.pmb.paymybuddy.exception.AlreadyExistsException;
import com.pmb.paymybuddy.exception.DataNoteFoundException;
import com.pmb.paymybuddy.model.Account;
import com.pmb.paymybuddy.model.BankAccount;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.repository.AccountRepository;
import com.pmb.paymybuddy.repository.BankAccountRepository;
import com.pmb.paymybuddy.util.AccountMapper;
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
     * @throws DataNoteFoundException
     */
    @Override
    public AccountDTO getAccount(String mail) throws DataNoteFoundException {
        log.info("getting account with email {}", mail);
        Account account = accountRepository.findByMail(mail).orElseThrow(() ->
                new DataNoteFoundException("no account found with email address " + mail));

        return accountMapper.toAccountDTO(account);

    }



}
