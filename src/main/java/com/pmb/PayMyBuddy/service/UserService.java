package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.DTO.ProfileDTO;
import com.pmb.PayMyBuddy.DTO.SignupDTO;
import com.pmb.PayMyBuddy.exceptions.AlreadyExistsException;
import com.pmb.PayMyBuddy.exceptions.BalanceNotEmptyException;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.AppUser;
import com.pmb.PayMyBuddy.repository.RoleRepository;
import com.pmb.PayMyBuddy.repository.UserRepository;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.util.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * contain all business service methods for user
 */
@Service
@Transactional
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PrincipalUser principalUser;
    private String message;
    @Autowired
    public UserService(UserRepository userRepository, AccountMapper accountMapper, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository, PrincipalUser principalUser) {
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.principalUser = principalUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean deleteUser() throws DataNotFoundException, BalanceNotEmptyException {
        logger.info("deleting account of {} ", principalUser.getCurrentUserMail());
        AppUser appUserToDelete = userRepository.findByAccount_Mail(principalUser.getCurrentUserMail())
                .orElseThrow(() -> new DataNotFoundException("Account not found"));
        if (appUserToDelete.getAccount().getBalance() != 0) {
            message = "account not empty balance =" + appUserToDelete.getAccount().getBalance();
            logger.error(message);
            throw new BalanceNotEmptyException(message);
        } else {
            appUserToDelete.removeAccount(appUserToDelete);
            userRepository.deleteById(appUserToDelete.getUserID());
            return true;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ProfileDTO addUser(SignupDTO newUser) throws AlreadyExistsException {
        logger.info("saving account of {} {}", newUser.getFirstName(), newUser.getLastName());
        if (userRepository.findByAccount_Mail(newUser.getMail()).isPresent()) {
            message = "an account already exists with this email ";
            logger.error(message, newUser.getMail());
            throw new AlreadyExistsException(message + newUser.getMail());
        }
        AppUser newAppUser = new AppUser(newUser.getFirstName(), newUser.getLastName(), newUser.getBirthDate());
        Account newAccount = new Account(newUser.getMail(), passwordEncoder.encode(newUser.getPassword()), newAppUser);
        newAccount.setRole(roleRepository.findByName("USER"));
        newAccount.setEnabled(true);
        newAppUser.setAccount(newAccount);

        userRepository.save(newAppUser);
        return accountMapper.toProfileDTO(newAccount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProfileDTO updateUser(SignupDTO userToUpdate) {
        logger.info("updating account of {} {}", userToUpdate.getFirstName(), userToUpdate.getLastName());
        AppUser appUser = userRepository.findByAccount_Mail(principalUser.getCurrentUserMail()).get();
        appUser.setFirstName(userToUpdate.getFirstName());
        appUser.setLastName(userToUpdate.getLastName());
        appUser.setBirthDate(userToUpdate.getBirthDate());
        if (userToUpdate.getPassword() != null) {
            appUser.getAccount().setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        }
        appUser.getAccount().setMail(userToUpdate.getMail());
        appUser = userRepository.save(appUser);
        return accountMapper.toProfileDTO(appUser.getAccount());

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ProfileDTO getUser() throws DataNotFoundException {
        logger.info("get current user");
        AppUser appUser = userRepository.findByAccount_Mail(principalUser.getCurrentUserMail()).orElseThrow(() -> new DataNotFoundException("account not found"));
        return accountMapper.toProfileDTO(appUser.getAccount());
    }
}
