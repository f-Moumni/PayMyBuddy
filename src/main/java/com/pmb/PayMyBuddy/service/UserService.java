package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.AccountDTO;
import com.pmb.PayMyBuddy.DTO.UserDTO;
import com.pmb.PayMyBuddy.constants.Roles;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.AppUser;
import com.pmb.PayMyBuddy.repository.RoleRepository;
import com.pmb.PayMyBuddy.repository.UserRepository;
import com.pmb.PayMyBuddy.security.PasswordEncoder;
import com.pmb.PayMyBuddy.util.AccountMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
@Slf4j

public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, AccountMapper accountMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    private String message;

    @Override
    public Boolean deleteUser(String mail) throws DataNotFoundException, InsufficientFundsException {
        log.info("deleting account of {} ", mail);
        AppUser appUserToDelete = userRepository.findByAccount_Mail(mail)
                .orElseThrow(() -> new DataNotFoundException("Account not found"));
        if (appUserToDelete.getAccount().getBalance() != 0) {
            message = "account not empty balance =" + appUserToDelete.getAccount().getBalance();
            log.error(message);
            throw new InsufficientFundsException(message);
        } else {
            appUserToDelete.removeAccount(appUserToDelete);
            userRepository.deleteById(appUserToDelete.getUserID());
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
        AppUser newAppUser = new AppUser(newUserDTO.getFirstName(), newUserDTO.getLastName(), newUserDTO.getBirthDate());
        Account newAccount = new Account(newUserDTO.getMail(), passwordEncoder.bCryptPasswordEncoder().encode(newUserDTO.getPassword()), newAppUser);
        newAccount.setRole(roleRepository.findByName(String.valueOf(Roles.USER)));
        newAppUser.setAccount(newAccount);

        userRepository.save(newAppUser);
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
    public AccountDTO updateUser(UserDTO userToUpdate) {
        log.info("updating account of {} {}", userToUpdate.getFirstName(), userToUpdate.getLastName());
        AppUser appUser = userRepository.findByAccount_Mail(userToUpdate.getMail()).get();
        appUser.setFirstName(userToUpdate.getFirstName());
        appUser.setLastName(userToUpdate.getLastName());
        appUser.setBirthDate(userToUpdate.getBirthDate());
        appUser.getAccount().setPassword(passwordEncoder.bCryptPasswordEncoder().encode(userToUpdate.getPassword()));
        appUser.getAccount().setMail(userToUpdate.getMail());
        appUser = userRepository.save(appUser);
        return accountMapper.toAccountDTO(appUser.getAccount());

    }

    @Override
    public AccountDTO getUser(String mail) throws DataNotFoundException {
        AppUser appUser = userRepository.findByAccount_Mail(mail).orElseThrow(() -> new DataNotFoundException("account not found"));
        return accountMapper.toAccountDTO(appUser.getAccount());
    }
}
