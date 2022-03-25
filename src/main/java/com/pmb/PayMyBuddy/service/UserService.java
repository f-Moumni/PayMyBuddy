package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.AccountDTO;
import com.pmb.PayMyBuddy.DTO.UserDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.User;
import com.pmb.PayMyBuddy.repository.UserRepository;
import com.pmb.PayMyBuddy.util.AccountMapper;
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
    public Boolean deleteUser(String mail) throws DataNotFoundException, InsufficientFundsException {
        log.info("deleting account of {} ",mail);
        User userToDelete = userRepository.findByAccount_Mail(mail)
                .orElseThrow(() -> new DataNotFoundException("Account not found"));
        if (userToDelete.getAccount().getBalance() != 0) {
            message = "account not empty balance =" + userToDelete.getAccount().getBalance();
            log.error(message);
            throw new InsufficientFundsException(message);
        } else {
            userToDelete.removeAccount(userToDelete);
            userRepository.deleteById(userToDelete.getUserID());
            return true;
        }
    }

    @Override
    public AccountDTO addUser(UserDTO newUserDTO) throws AlreadyExistsException {
        log.info("saving account of {} {}", newUserDTO.getFirstName(), newUserDTO.getLastName());
        if (userRepository.findByAccount_Mail(newUserDTO.getMail()).isPresent()) {
            message = "an account already exists with this email{} ";
            log.error(message, newUserDTO.getMail());
            throw new AlreadyExistsException(message + newUserDTO.getMail());
        }
        User newUser = new User(newUserDTO.getFirstName(), newUserDTO.getLastName(), newUserDTO.getBirthDate());
        Account newAccount = new Account(newUserDTO.getMail(), newUserDTO.getPassword(), newUser);
        newUser.setAccount(newAccount);
        userRepository.save(newUser);
        return accountMapper.toAccountDTO(newAccount);
    }

    /**
     * update given account
     *
     * @param userToUpdate
     * @return
     * @throws DataNotFoundException
     */
    @Override
    public AccountDTO updateUser(UserDTO userToUpdate)  {
        log.info("updating account of {} {}", userToUpdate.getFirstName(), userToUpdate.getLastName());
        User user = userRepository.findByAccount_Mail(userToUpdate.getMail()).get();
        user.setFirstName(userToUpdate.getFirstName());
        user.setLastName(userToUpdate.getLastName());
        user.setBirthDate(userToUpdate.getBirthDate());
        user.getAccount().setPassword(userToUpdate.getPassword());
        user.getAccount().setMail(userToUpdate.getMail());
        user = userRepository.save(user);
        return accountMapper.toAccountDTO(user.getAccount());

    }

    @Override
    public AccountDTO getUser(String mail) throws DataNotFoundException {
        User user = userRepository.findByAccount_Mail(mail).orElseThrow(()-> new DataNotFoundException("account not found"));
        return accountMapper.toAccountDTO(user.getAccount());
    }
}
