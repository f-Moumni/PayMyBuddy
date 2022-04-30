package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.PaymentDTO;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;

import java.util.List;

/**
 * contain all business service methods for payment
 */
public interface IPaymentService {

      /** send money to contact
     * @param paymentDTO
     * @return tue is payment is done
     * @throws InsufficientFundsException
     */
    boolean doPayment(PaymentDTO paymentDTO) throws InsufficientFundsException;

    /**
     * get All Payments
     * @return
     */
    List<TransactionDTO> getAllPayments();
}
