package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.DTO.PaymentDTO;
import com.pmb.paymybuddy.DTO.TransactionDTO;
import com.pmb.paymybuddy.DTO.TransferDTO;
import com.pmb.paymybuddy.exception.BalanceException;
import com.pmb.paymybuddy.exception.DataNoteFoundException;

import java.util.Set;

public interface ITransactionService {
    boolean addPayment(PaymentDTO paymentDTO) throws BalanceException;

    boolean addTransfer(TransferDTO transferToAdd) throws BalanceException, DataNoteFoundException;

    Set<TransactionDTO> getAllTransactions(String email);
}
