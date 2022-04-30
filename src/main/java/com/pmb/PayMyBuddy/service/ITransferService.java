package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;

import java.util.List;

/**
 * contain all business service methods for Transfer
 */
public interface ITransferService {

    /**
     * do transfer with bank account
     * @param transferToAdd
     * @return true if is done
     * @throws DataNotFoundException
     * @throws InsufficientFundsException
     */
    boolean doTransfer(TransferDTO transferToAdd) throws DataNotFoundException, InsufficientFundsException;

    /**
     * get all transfers of current user
     * @return
     */
    List<TransactionDTO> getAllTransfers();
}
