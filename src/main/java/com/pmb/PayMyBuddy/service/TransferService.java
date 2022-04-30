package com.pmb.PayMyBuddy.service;


import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;

import com.pmb.PayMyBuddy.DTO.TransferDTO;
import com.pmb.PayMyBuddy.exceptions.DataNotFoundException;

import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.model.Account;

import com.pmb.PayMyBuddy.model.Payment;
import com.pmb.PayMyBuddy.model.Transfer;
import com.pmb.PayMyBuddy.repository.AccountRepository;

import com.pmb.PayMyBuddy.repository.TransferRepository;
import com.pmb.PayMyBuddy.security.PrincipalUser;
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
public class TransferService implements ITransferService{


    private final TransferRepository transferRepository;

    private final AccountRepository accountRepository;

    private final TransactionMapper transactionMapper;

    private final PrincipalUser principalUser;
    private final Calculator calculator;
    private double fee;
    private double total;

    @Autowired
    public TransferService(TransferRepository transferRepository, AccountRepository accountRepository, TransactionMapper transactionMapper, PrincipalUser principalUser, Calculator calculator) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
        this.principalUser = principalUser;
        this.calculator = calculator;
    }

    /**
     * sen money to bank account
     *
     * @return true if transfer is done
     * @throws InsufficientFundsException
     * @throws DataNotFoundException
     */
    @Override
    public boolean doTransfer(TransferDTO transferToAdd) throws DataNotFoundException, InsufficientFundsException {
        Account account = accountRepository.findByMail(principalUser.getCurrentUserMail()).get();
        log.info("saving transfer for {}", account.getAccountOwner().getFirstName());
        if (account.getAccountOwner().getBankAccount() == null) { //bank account verification
            log.error("no bank account attached to this account ");
            throw new DataNotFoundException("bank account not found ");
        }
        fee = calculator.feeCalculator(transferToAdd.getAmount());
        total = calculator.totalCalculator(transferToAdd.getAmount());
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
     * @return set of transactions
     */

    private List<TransactionDTO> getSentTransfers() {

        List<Transfer> transfers = new ArrayList<>();
        transferRepository.findByDebitAccount(principalUser.getCurrentUserMail()).forEach(transfers::add);
        return transfers.stream()
                .map(transfer -> transactionMapper.transferMapper(transfer, OperationType.DEBIT))
                .collect(Collectors.toList());

    }

    private List<TransactionDTO> getReservedTransfers() {

        List<Transfer> transfers = new ArrayList<>();
        transferRepository.findByCreditAccount(principalUser.getCurrentUserMail()).forEach(transfers::add);
        return transfers.stream()
                .map(transfer -> transactionMapper.transferMapper(transfer, OperationType.CREDIT))
                .collect(Collectors.toList());
    }
    @Override
    public List<TransactionDTO> getAllTransfers() {
        return Stream.concat(getReservedTransfers().stream(), getSentTransfers().stream())
                .sorted(Comparator.comparing(TransactionDTO::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private void updatePMBAccount(double fee) {
        Account pmbAccount = accountRepository.findById(1L).get();
        pmbAccount.setBalance(pmbAccount.getBalance() + fee);
        accountRepository.save(pmbAccount);
    }
}
