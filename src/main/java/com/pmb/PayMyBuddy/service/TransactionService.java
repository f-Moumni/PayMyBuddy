package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.DTO.PaymentDTO;
import com.pmb.paymybuddy.DTO.TransactionDTO;
import com.pmb.paymybuddy.DTO.TransferDTO;
import com.pmb.paymybuddy.exception.BalanceException;
import com.pmb.paymybuddy.exception.DataNoteFoundException;
import com.pmb.paymybuddy.model.Account;
import com.pmb.paymybuddy.model.Payment;
import com.pmb.paymybuddy.model.Transfer;
import com.pmb.paymybuddy.repository.AccountRepository;
import com.pmb.paymybuddy.repository.PaymentRepository;
import com.pmb.paymybuddy.repository.TransferRepository;
import com.pmb.paymybuddy.util.TransactionMapper;
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
public class TransactionService implements ITransactionService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionMapper transactionMapper;
    private double fee;
    private final float feePercentage = 0.5F;

    /**
     * send money to contact
     *
     * @param paymentDTO
     * @return tue is payment is done
     * @throws DataNoteFoundException
     * @throws BalanceException
     */
    @Override
    public boolean addPayment(PaymentDTO paymentDTO) throws BalanceException {
        Account accountDebit = accountRepository.findByMail(paymentDTO.getDebitAccountEmail()).get();
        Account accountCredit = accountRepository.findByMail(paymentDTO.getCreditAccountEmail()).get();
        log.info("saving payment to {} {}", accountCredit.getAccountOwner().getFirstName(),
                accountCredit.getAccountOwner().getLastName());
        if (accountDebit.isActive() &&
                accountCredit.isActive() && accountDebit.getAccountOwner().getContacts()
                .contains(accountCredit.getAccountOwner())) {//account is active
            fee = (paymentDTO.getAmount() * feePercentage) / 100;
            double total = fee + paymentDTO.getAmount();
            if (accountDebit.getBalance() >= (total)) { // account has enough balance
                accountCredit.setBalance(accountCredit.getBalance() + paymentDTO.getAmount());
                accountDebit.setBalance(accountDebit.getBalance() - total);
                accountRepository.save(accountCredit);
                accountRepository.save(accountDebit);
                paymentRepository.save(
                        new Payment(paymentDTO.getAmount(), fee, paymentDTO.getDescription(), now(), accountDebit, accountCredit));
            } else {
                throw new BalanceException("poor balance");
            }
        }
        updatePMBAccount();// update admin account

        return true;
    }

    /**
     * sen money to bank account
     *
     * @param transferToAdd
     * @return true transfer is done
     * @throws BalanceException
     * @throws DataNoteFoundException
     */
    @Override
    public boolean addTransfer(TransferDTO transferToAdd) throws BalanceException, DataNoteFoundException {
        Account accountDebit = accountRepository.findByMail(transferToAdd.getAccountEmail()).get();
        log.info("saving transfer for {}", accountDebit.getAccountOwner().getFirstName());
        if (accountDebit.getAccountOwner().getBankAccount() == null) { //bank account verification
            log.error("no bank account attached to this account ");
            throw new DataNoteFoundException("bank account not found ");
        }
        if (accountDebit.isActive()) {//account is active
            fee = (transferToAdd.getAmount() * feePercentage) / 100;
            double total = fee + transferToAdd.getAmount();
            if (accountDebit.getBalance() >= (total)) { // account has enough balance
                accountDebit.setBalance(accountDebit.getBalance() - total);
                accountRepository.save(accountDebit);
                transferRepository.save(new Transfer(transferToAdd.getAmount(), fee, transferToAdd.getDescription(), now(), accountDebit, accountDebit.getAccountOwner().getBankAccount()));
            } else {
                throw new BalanceException("poor balance");
            }
        }
        updatePMBAccount();// update admin account

        return true;
    }

    /**
     * get all transaction to a given account
     *
     * @param email
     * @return set of transactions
     */
    @Override
    public Set<TransactionDTO> getAllTransactions(String email) {
        List<Payment> sent = new ArrayList<>(), received = new ArrayList<>();
        paymentRepository.findByDebitAccount(email).forEach(sent::add);
        paymentRepository.findByCreditAccount(email).forEach(received::add);
        List<Transfer> transfers = new ArrayList<>();
        transferRepository.findByDebitAccount(email).forEach(transfers::add);
        // mapping sent payments to TransactionDTO
        List<TransactionDTO> paymentSent = sent.stream().map(transactionMapper::paymentSentMapper).collect(Collectors.toList());
        // mapping received payments to TransactionDTO
        List<TransactionDTO> paymentsReceived = received.stream().map(transactionMapper::paymentReceivedMapper).collect(Collectors.toList());
        // mapping bank transactions to TransactionDTO
        List<TransactionDTO> bankTransfers = transfers.stream().map(transactionMapper::bankTransfers).collect(Collectors.toList());
        List<TransactionDTO> result = Stream.concat(paymentSent.stream(), paymentsReceived.stream()).collect(Collectors.toList());
        return Stream.concat(result.stream(), bankTransfers.stream()).collect(Collectors.toSet());
    }

    private void updatePMBAccount() {
        Account pmbAccount = accountRepository.findById(1L).get();
        pmbAccount.setBalance(pmbAccount.getBalance() + fee);
        accountRepository.save(pmbAccount);
    }
}
