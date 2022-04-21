package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Service
@Transactional
@Slf4j
public class TransactionService {

    @Autowired
    PaymentService paymentService;
    @Autowired
    TransferService transferService;


    public List<TransactionDTO> getAllTransactions() {
        return Stream.concat(paymentService.getAllPayments().stream(),transferService.getAllTransfers().stream()
        ).collect(Collectors.toList());
    }
}
