package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.PaymentDTO;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;

import java.util.List;

public interface IPaymentService {
    boolean doPayment(PaymentDTO paymentDTO) throws InsufficientFundsException;

    List<TransactionDTO> getAllPayments();
}
