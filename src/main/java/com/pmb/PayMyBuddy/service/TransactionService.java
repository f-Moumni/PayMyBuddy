package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Service
@Transactional
@Slf4j
public class TransactionService {


  private final  PaymentService paymentService;
   private final  TransferService transferService;
@Autowired
    public TransactionService(PaymentService paymentService, TransferService transferService) {
        this.paymentService = paymentService;
        this.transferService = transferService;
    }


    public List<TransactionDTO> getAllTransactions() {
        return Stream.concat(paymentService.getAllPayments().stream(),transferService.getAllTransfers().stream()
        ).collect(Collectors.toList());
    }
}
