package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TransactionService class
 */
@Service
@Transactional
@Slf4j
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final PaymentService paymentService;
    private final TransferService transferService;

    @Autowired
    public TransactionService(PaymentService paymentService, TransferService transferService) {
        this.paymentService = paymentService;
        this.transferService = transferService;
    }

    /**
     * get All transactions of current user
     *
     * @return
     */
    public List<TransactionDTO> getAllTransactions() {
        logger.info("get all transaction ");
        return Stream.concat(paymentService.getAllPayments().stream(), transferService.getAllTransfers().stream())
                .sorted(Comparator.comparing(TransactionDTO::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}
