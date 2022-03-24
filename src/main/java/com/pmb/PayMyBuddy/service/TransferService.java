package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;

import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.model.Account;

import com.pmb.PayMyBuddy.model.Transfer;
import com.pmb.PayMyBuddy.repository.AccountRepository;

import com.pmb.PayMyBuddy.repository.TransferRepository;
import com.pmb.PayMyBuddy.util.Calculator;
import com.pmb.PayMyBuddy.util.TransactionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;


@Service
@Transactional
@Slf4j
public class TransferService {


    @Autowired
    TransferRepository transferRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionMapper transactionMapper;
    private double fee;
    private double total;

    /**
     * sen money to bank account
     *
     * @return true transfer is done
     * @throws InsufficientFundsException
     * @throws DataNotFoundException
     */

    public boolean doTransfer(TransferDTO transferToAdd) throws DataNotFoundException, InsufficientFundsException {
        Account account = accountRepository.findByMail(transferToAdd.getAccountEmail()).get();
        log.info("saving transfer for {}", account.getAccountOwner().getFirstName());
        if (account.getAccountOwner().getBankAccount() == null) { //bank account verification
            log.error("no bank account attached to this account ");
            throw new DataNotFoundException("bank account not found ");
        }
        fee = Calculator.feeCalculator(transferToAdd.getAmount());
        total = Calculator.totalCalculator(transferToAdd.getAmount());
        if (transferToAdd.getOperationType().equals(OperationType.CREDIT)) {
            account.setBalance(account.getBalance() + transferToAdd.getAmount());
            transferRepository.save(new Transfer(transferToAdd.getAmount(), fee, transferToAdd.getDescription(), now(), null, account, account.getAccountOwner().getBankAccount()));
        } else if (account.getBalance() >= (total)) { // account has enough balance
            account.setBalance(account.getBalance() - total);
            transferRepository.save(new Transfer(transferToAdd.getAmount(), fee, transferToAdd.getDescription(), now(), account, null, account.getAccountOwner().getBankAccount()));
        } else {
            throw new InsufficientFundsException("poor balance");
        }
        accountRepository.save(account);
        updatePMBAccount(fee);// update admin account
        return true;

    }

    /**
     * get all transaction to a given account
     *
     * @param email
     * @return set of transactions
     */

    public List<TransactionDTO> getSentTransfers(String email) {
       // Account userAccount = accountRepository.findByMail(email).get();
        List<Transfer>  transfers = new ArrayList<>();
        transferRepository.findByDebitAccount(email).forEach(transfers::add);
        return transfers.stream()
                .map(transfer -> transactionMapper.transferMapper(transfer, OperationType.DEBIT)).collect(Collectors.toList());
        /*List<Payment> sent = new ArrayList<>(), received = new ArrayList<>();
        //   paymentRepository.findByDebitAccount(email).forEach(sent::add);
        //  paymentRepository.findByCreditAccount(email).forEach(received::add);
        List<Transfer> transfers = new ArrayList<>();
        transferRepository.findByDebitAccount(email).forEach(transfers::add);
        // mapping sent payments to TransactionDTO
        List<TransactionDTO> paymentSent = sent.stream().map(transactionMapper::paymentSentMapper).collect(Collectors.toList());
        // mapping received payments to TransactionDTO
        List<TransactionDTO> paymentsReceived = received.stream().map(transactionMapper::paymentReceivedMapper).collect(Collectors.toList());
        // mapping bank transactions to TransactionDTO
        List<TransactionDTO> bankTransfers = transfers.stream().map(transactionMapper::bankTransfers).collect(Collectors.toList());
        List<TransactionDTO> result = Stream.concat(paymentSent.stream(), paymentsReceived.stream()).collect(Collectors.toList());
        return Stream.concat(result.stream(), bankTransfers.stream()).collect(Collectors.toSet());*/
    }

    public List<TransactionDTO> getReservedTransfers(String email) {
      //  Account userAccount = accountRepository.findByMail(email).get();
        List<Transfer>  transfers = new ArrayList<>();
        transferRepository.findByCreditAccount(email).forEach(transfers::add);
        return transfers.stream()
                .map(transfer -> transactionMapper.transferMapper(transfer, OperationType.CREDIT)).collect(Collectors.toList());
    }

    public List<TransactionDTO> getAllTransfers(String email) {

        return Stream.concat(getReservedTransfers(email).stream(), getSentTransfers(email).stream()).collect(Collectors.toList());
    }

    private void updatePMBAccount(double fee) {
        Account pmbAccount = accountRepository.findById(1L).get();
        pmbAccount.setBalance(pmbAccount.getBalance() + fee);
        accountRepository.save(pmbAccount);
    }
}
