package com.pmb.PayMyBuddy.service;



import com.pmb.PayMyBuddy.DTO.AccountDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface IAccountService {


    AccountDTO getAccount(String mail) throws DataNotFoundException;



}
