package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.DTO.PaymentDTO;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.Payment;
import com.pmb.PayMyBuddy.repository.AccountRepository;
import com.pmb.PayMyBuddy.repository.PaymentRepository;
import com.pmb.PayMyBuddy.repository.TransferRepository;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.util.Calculator;
import com.pmb.PayMyBuddy.util.TransactionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;

@Service
@Transactional
@Slf4j
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionMapper transactionMapper;
    private double fee;
    private double total;

    /**
     * send money to contact
     *
     * @param paymentDTO
     * @return tue is payment is done
     * @throws InsufficientFundsException
     */

    public boolean doPayment(PaymentDTO paymentDTO) throws InsufficientFundsException {

        Account accountDebit = accountRepository.findByMail(PrincipalUser.getCurrentUserMail()).get();
        Account accountCredit = accountRepository.findByMail(paymentDTO.getCreditAccountEmail()).get();
        log.info("saving payment to {} {}", accountCredit.getAccountOwner().getFirstName(),
                accountCredit.getAccountOwner().getLastName());
        if (accountDebit.isEnabled() &&//account is active
                accountCredit.isEnabled() && accountDebit.getAccountOwner().getContacts()
                .contains(accountCredit.getAccountOwner())) {// credit account is in contact list
            fee = Calculator.feeCalculator(paymentDTO.getAmount());
            total = Calculator.totalCalculator(paymentDTO.getAmount());
            if (accountDebit.getBalance() >= total) { // account has enough balance
                accountCredit.setBalance(accountCredit.getBalance() + paymentDTO.getAmount());
                accountDebit.setBalance(accountDebit.getBalance() - total);
                accountRepository.save(accountCredit);
                accountRepository.save(accountDebit);
                paymentRepository.save(
                        new Payment(paymentDTO.getAmount(), fee, paymentDTO.getDescription(), now(), accountDebit, accountCredit));
            } else {
                throw new InsufficientFundsException("poor balance");
            }
        }
        updatePMBAccount(fee);// update admin account

        return true;
    }

    public List<TransactionDTO> getSentPayments(String email) {

        List<Payment> payments = new ArrayList<>();
        paymentRepository.findByDebitAccount(email).forEach(payments::add);
        return payments.stream()
                .map(payment -> transactionMapper.PaymentMapper(payment, OperationType.DEBIT))
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getReceivedPayments(String email) {
        List<Payment> payments = new ArrayList<>();
        paymentRepository.findByCreditAccount(email).forEach(payments::add);
        return payments.stream()
                .map(payment -> transactionMapper.PaymentMapper(payment, OperationType.CREDIT))
                .collect(Collectors.toList());


    }

    public List<TransactionDTO> getAllPayments(String email) {
        return Stream.concat(getReceivedPayments(email).stream()
                , getSentPayments(email).stream()
        ).collect(Collectors.toList());
    }

    private void updatePMBAccount(double fee) {
        Account pmbAccount = accountRepository.findById(1L).get();
        pmbAccount.setBalance(pmbAccount.getBalance() + fee);
        accountRepository.save(pmbAccount);
    }
}
