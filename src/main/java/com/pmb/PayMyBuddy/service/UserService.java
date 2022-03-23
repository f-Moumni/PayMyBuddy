package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.DTO.AccountDTO;
import com.pmb.paymybuddy.exception.AlreadyExistsException;
import com.pmb.paymybuddy.exception.BalanceException;
import com.pmb.paymybuddy.exception.DataNoteFoundException;
import com.pmb.paymybuddy.model.Account;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.repository.UserRepository;
import com.pmb.paymybuddy.util.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
@Slf4j
public class UserService implements IUserService {
    @Autowired
    UserRepository userRepository;
    private String message;
    @Autowired
    AccountMapper accountMapper;


    @Override
    public Boolean deleteUser(AccountDTO accountToDelete) throws DataNoteFoundException, BalanceException {
        log.info("deleting account of {} {}", accountToDelete.getFirstName(), accountToDelete.getLastName());
        User userToDelete = userRepository.findByAccount_Mail(accountToDelete.getMail())
                .orElseThrow(() -> new DataNoteFoundException("Account not found"));
        if (userToDelete.getAccount().getBalance() != 0) {
            message = "account not empty balance =" + userToDelete.getAccount().getBalance();
            log.error(message);
            throw new BalanceException(message);
        } else {
            userToDelete.removeAccount(userToDelete);
            userRepository.deleteById(userToDelete.getUserID());
            return true;
        }
    }

    @Override
    public AccountDTO addUser(AccountDTO accountDTO) throws AlreadyExistsException {
        log.info("saving account of {} {}", accountDTO.getFirstName(), accountDTO.getLastName());
        if (userRepository.findByAccount_Mail(accountDTO.getMail()).isPresent()) {
            message = "an account already exists with this email{} ";
            log.error(message, accountDTO.getMail());
            throw new AlreadyExistsException(message + accountDTO.getMail());
        }
        User newUser = new User(accountDTO.getFirstName(), accountDTO.getLastName(), accountDTO.getBirthDate());
        Account newAccount = new Account(accountDTO.getMail(), accountDTO.getPassword(), newUser);
        newUser.setAccount(newAccount);
        userRepository.save(newUser);
        return accountMapper.toAccountDTO(newAccount);
    }

    /**
     * update given account
     *
     * @param accountToUpdate
     * @return
     * @throws DataNoteFoundException
     */
    @Override
    public AccountDTO updateUser(AccountDTO accountToUpdate) throws DataNoteFoundException {
        log.info("updating account of {} {}", accountToUpdate.getFirstName(), accountToUpdate.getLastName());
        User user = userRepository.findByAccount_Mail(accountToUpdate.getMail()).get();
        user.setFirstName(accountToUpdate.getFirstName());
        user.setLastName(accountToUpdate.getLastName());
        user.setBirthDate(accountToUpdate.getBirthDate());
        user.getAccount().setPassword(accountToUpdate.getPassword());
        user.getAccount().setMail(accountToUpdate.getMail());
        user = userRepository.save(user);
        return accountMapper.toAccountDTO(user.getAccount());

    }

    @Override
    public AccountDTO getUser(String mail) throws DataNoteFoundException {
        User user = userRepository.findByAccount_Mail(mail).orElseThrow(()-> new DataNoteFoundException("account not found"));
        return accountMapper.toAccountDTO(user.getAccount());
    }
}
