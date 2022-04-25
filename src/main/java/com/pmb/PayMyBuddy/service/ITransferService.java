package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;

import java.util.List;

public interface ITransferService {
    boolean doTransfer(TransferDTO transferToAdd) throws DataNotFoundException, InsufficientFundsException;

    List<TransactionDTO> getAllTransfers();
}
