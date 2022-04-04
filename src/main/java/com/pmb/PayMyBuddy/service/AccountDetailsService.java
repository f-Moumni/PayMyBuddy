package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.AppUser;
import com.pmb.PayMyBuddy.repository.AccountRepository;
import com.pmb.PayMyBuddy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service

public class AccountDetailsService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "user with email %S not found";
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        AppUser user = userRepository.findByAccount_Mail(mail)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, mail)));
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getAccount().getRole().getName());
        return new User(user.getAccount().getMail(),
                user.getAccount().getPassword(), Arrays.asList(authority));


    }
}
