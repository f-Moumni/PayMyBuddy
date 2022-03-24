package com.pmb.PayMyBuddy.service;



import com.pmb.PayMyBuddy.DTO.PaymentDTO;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;


import java.util.Set;

public interface ITransactionService {

    boolean doPayment(PaymentDTO paymentDTO) throws InsufficientFundsException;

    boolean doTransfer(TransferDTO transferToAdd) throws DataNotFoundException, InsufficientFundsException;

    Set<TransactionDTO> getAllTransactions(String email);
}
